package com.sample.coolplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.api.*
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Verifies if the plugin supports Android Gradle plugin of version 3.0 or 3.1.
 */
internal fun Plugin<Project>.validateAndroidPluginVersion3or31(errorMessage: String) {
    var gradlePluginVersion: String? = null
    var exception: Exception? = null

    try {
        gradlePluginVersion = Class.forName("com.android.builder.Version")
            .getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION")
            .get(this)
            .toString()
    } catch (e: Exception) {
        exception = e
    }

    try {
        gradlePluginVersion = Class.forName("com.android.builder.model.Version")
            .getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION")
            .get(this)
            .toString()
    } catch (e: Exception) {
        exception = e
    }

    if (gradlePluginVersion == null && exception != null) {
        throw IllegalStateException(errorMessage, exception)
    } else if (gradlePluginVersion == null) {
        throw IllegalStateException(errorMessage)
    }
}

/**
 * Returns the collection of Android variants on the basis of applied plugin. If the Android plugins are missing we
 * are throwing build error.
 */
internal fun Project.getAndroidVariantsOrThrow(errorMessage: String): DomainObjectCollection<out BaseVariant> {
    return when {
        this.plugins.hasPlugin("com.android.application") -> {
            val ext = project.extensions.findByType(AppExtension::class.java)
            ext!!.applicationVariants
        }

        this.plugins.hasPlugin("com.android.test") -> {
            val ext = project.extensions.findByType(TestExtension::class.java)
            ext!!.applicationVariants
        }

        this.plugins.hasPlugin("com.android.library") -> {
            val ext = project.extensions.findByType(LibraryExtension::class.java)
            ext!!.libraryVariants
        }

        else -> throw IllegalArgumentException(errorMessage)
    }
}

/**
 * Yields all available variants of type [TestVariant].
 */
internal fun DomainObjectCollection<out BaseVariant>.forEachTestVariant(block: (TestVariant) -> Unit) {
    all { variant ->
        when (variant) {
            is ApplicationVariant -> variant.testVariant?.let(block)
            is TestVariant -> block(variant)
            is LibraryVariant -> variant.testVariant?.let(block)
        }
    }
}

/**
 * Yields packaging output for the [TestVariant].
 */
internal fun TestVariant.forEachApkOutput(block: (ApkVariantOutput) -> Unit) {
    outputs.all { output ->
        if (output is ApkVariantOutput) {
            block(output)
        } else {
            throw IllegalArgumentException("Unexpected output type for variant $name: ${output::class.java}")
        }
    }
}