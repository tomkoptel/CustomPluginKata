package com.sample.coolplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import java.io.File

/**
 * Make sure the task is open. Otherwise the plugin will fail with exception:
 * org.gradle.api.GradleException: Could not generate a proxy class for class com.sample.coolplugin.CoolTask.
 */
open class CoolTask : DefaultTask() {
    // Represents path to the instrumentation task that get generated for the tests
    @InputFile
    var instrumentationApkFile: File? = null
}