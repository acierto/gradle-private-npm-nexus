# gradle-private-npm-nexus
Gradle plugin which generate necessary configuration to download npm packages from the private Nexus

When you start using your private Nexus to publish NPM artifacts there and keep third party libraries proxied on your 
server you have to configure manually global npm configuration, for that it is required in <HOME_DIR>/.npmrc 
to add next lines:

_auth=... # login:password encrypted with base64 encoding
registry=... # path to your NPM group repository 
always-auth=true

How to create NPM group repository in your Nexus 3 read here: https://blog.sonatype.com/using-nexus-3-as-your-repository-part-2-npm-packages

In order to not force every developer to do this stuff manually, you can apply this plugin in your project
and corresponding 3 lines will be added to your .npmrc file (or file will be created if it doesn't exist now).

# Adding the plugin to your project 

```groovy
buildscript {
  repositories {
    mavenLocal()
    ["releases", "public", "snapshots", "alphas"].each { r ->
      maven {
        url "${nexusBaseUrl}/repositories/${r}"
        credentials {
          username nexusUserName
          password nexusPassword
        }
      }
    }
  }

  dependencies {
    classpath "com.acierto.gradle:gradle-private-npm-nexus:0.0.2"
  }
}

apply plugin: 'acierto.private-nexus'

privateNpmNexus {
  nexusGroupUrl='http://nexus.your.organisation.com/repository/npm-group/' // http url as an example
}

```

# Release a new version of plugin

```shell script
./gradlew release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=x.y.z
```
