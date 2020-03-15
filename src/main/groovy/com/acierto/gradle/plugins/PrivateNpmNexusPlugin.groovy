package com.acierto.gradle.plugins

import com.acierto.gradle.tasks.UpdateNpmRcTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class PrivateNpmNexusPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.afterEvaluate {
      createTasks(project)
    }
  }

  static void createTasks(Project project) {
    project.tasks.create("updateNpmRc", UpdateNpmRcTask)
  }
}
