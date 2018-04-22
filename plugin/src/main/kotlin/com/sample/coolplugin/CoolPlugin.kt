package com.sample.coolplugin

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class CoolPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        validateAndroidPluginVersion3or31("Version of Android plugin should be >= 3.0")
        val variants = project.getAndroidVariantsOrThrow("Android plugins are missing can not configure module")

        variants.forEachTestVariant { variant ->
            variant.forEachApkOutput { output ->
                var slug = variant.name.capitalize() // e.g. DebugAndroidTest
                var path = "${project.buildDir}/outputs/cool/${variant.name}"
                if (variant.outputs.size > 1) {
                    slug += output.name.capitalize()
                    path += "/${output.name}"
                }

                // Important condition to make our task cacheable with incremental build
                val taskOutputFile = project.file("$path/cool.txt")
                val instrumentationApk = output.outputFile
                val assembleAndroidTestTask = output.assemble

                project.tasks.create("cool$slug", CoolTask::class.java).apply {
                    description = "Does something cool for variant ${variant.name}."
                    group = "Verification"
                    outputs.file(taskOutputFile)

                    // Inject all necessary dependencies to the instance of [CoolTask].
                    instrumentationApkFile = instrumentationApk

                    // Configure the order in which task executes and its dependencies.
                    addTaskToGraph(assembleAndroidTestTask, this)
                }
            }
        }
    }

    private fun addTaskToGraph(parentTask: Task, ourTask: DefaultTask) {
        ourTask.dependsOn(parentTask)
        ourTask.mustRunAfter(parentTask)
        parentTask.finalizedBy(ourTask)
    }
}