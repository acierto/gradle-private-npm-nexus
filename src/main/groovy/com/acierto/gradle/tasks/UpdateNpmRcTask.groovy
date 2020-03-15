package com.acierto.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths

class UpdateNpmRcTask extends DefaultTask {

  private static void getNpmRc() {
    Paths.get(System.getProperty("user.home"), '.npmrc_test');
  }

  private void throwError(String msg) {
    project.logger.error(msg);
    throw new Error(msg);
  }

  private void checkProperties() {
    if (project.findProperty("nexusUserName") == null) {
      throwError('nexusUserName is not defined.');

    }
    if (project.findProperty("nexusPassword") == null) {
      throwError("nexusPassword is not defined.")
    }
  }

  @TaskAction
  def updateNpmRcFile() {
    checkProperties();
    def npmRcFile = project.file(getNpmRc());
    if (!npmRcFile.exists()) {
      npmRcFile.createNewFile();
      npmRcFile.write("registry=http://xl-nexus.xebialabs.com/repository/NPM_Group/");
      npmRcFile.write(  "_auth=Ym5lY2h5cG9yZW5rbzpOYjIxNjQ1MTE1JDg=");
      npmRcFile.write("always-auth=true");
    }
  }
}
