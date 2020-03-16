package com.acierto.gradle.tasks

import com.acierto.gradle.extentions.PrivateNpmNexusExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

class UpdateNpmRcTask extends DefaultTask {

  private static def getNpmRc(project) {
    Paths.get(project.gradle.gradleUserHomeDir.parent, '.npmrc')
  }

  private void throwError(String msg) {
    project.logger.error(msg)
    throw new Error(msg)
  }

  private def getNexusUserName() {
    project.findProperty("nexusUserName")
  }

  private def getNexusPassword() {
    project.findProperty("nexusPassword")
  }

  private def getAuth() {
    def username = getNexusUserName()
    def password = getNexusPassword()
    "$username:$password".bytes.encodeBase64().toString()
  }

  private static def getErrorMessage(fieldName) {
    "Check you <HOME_DIR>/.gradle/gradle.properties file. There [${fieldName}] has to be defined."
  }

  private void checkProperties() {
    if (getNexusUserName() == null) {
      throwError(getErrorMessage('nexusUsername'))

    }
    if (getNexusPassword() == null) {
      throwError(getErrorMessage('nexusPassword'))
    }
  }

  def getNpmRcProperties() {
    Properties properties = new Properties()
    File propertiesFile = project.file(getNpmRc(project))
    if (propertiesFile.exists()) {
      propertiesFile.withInputStream {
        properties.load(it)
      }
    }
    return properties
  }

  static String addPropertyIfMissing(String text, Properties properties, String key, String value) {
    if (properties.getProperty(key) == value) {
      text
    } else {
      text += "\n${key}=${value}\n"
      text
    }
  }

  @TaskAction
  def updateNpmRcFile() {
    checkProperties()
    def npmRcFile = project.file(getNpmRc(project))
    def npmRcProperties = getNpmRcProperties()
    if (!npmRcFile.exists()) {
      npmRcFile.createNewFile()
    }

    PrivateNpmNexusExtension extension = project.extensions.getByType(PrivateNpmNexusExtension)
    String text = npmRcFile.readLines().join("\n")
    text = addPropertyIfMissing(text, npmRcProperties, 'registry', extension.nexusGroupUrl)
    text = addPropertyIfMissing(text, npmRcProperties, '_auth', getAuth())
    text = addPropertyIfMissing(text, npmRcProperties, 'always-auth', 'true')
    npmRcFile.write(text)
  }
}
