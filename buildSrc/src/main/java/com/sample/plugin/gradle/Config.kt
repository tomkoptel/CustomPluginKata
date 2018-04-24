package com.sample.plugin.gradle

import com.sample.plugin.gradle.Config.Project.kotlinVersion
import org.gradle.api.JavaVersion

private const val supportVersion = "27.1.1"

object Config {
    object Project {
        const val kotlinVersion = "1.2.30"
        val javaVersion = JavaVersion.VERSION_1_7
    }

    object BuildPlugins {
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
        const val anroidGradlePlugin = "com.android.tools.build:gradle:3.1.1"
    }

    object Android {
        const val buildToolsVersion = "27.0.3"
        const val minSdkVersion = 19
        const val targetSdkVersion = 27
        const val compileSdkVersion = 27
        const val versionCode = 1
        const val versionName = "0.1"
    }

    object Libs {
        const val appcompat = "com.android.support:appcompat-v7:$supportVersion"
        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    }

    object TestLibs {
        const val junit = "junit:junit:4.12"
        const val spockCore = "org.spockframework:spock-core:1.1-groovy-2.4"
    }
}

val buildPlugins = Config.BuildPlugins
val projectConfig = Config.Project
val androidDefaults = Config.Android
val libs = Config.Libs
val testLibs = Config.TestLibs