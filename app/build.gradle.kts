import com.sample.plugin.gradle.Config.Android.buildToolsVersion
import com.sample.plugin.gradle.Config.Android.compileSdkVersion
import com.sample.plugin.gradle.Config.Android.minSdkVersion
import com.sample.plugin.gradle.Config.Android.targetSdkVersion
import com.sample.plugin.gradle.Config.Android.versionCode
import com.sample.plugin.gradle.Config.Android.versionName
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.setValue
import com.sample.plugin.gradle.androidDefaults
import com.sample.plugin.gradle.libs

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(androidDefaults.compileSdkVersion)
    buildToolsVersion(androidDefaults.buildToolsVersion)

    defaultConfig {
        minSdkVersion(androidDefaults.minSdkVersion)
        targetSdkVersion(androidDefaults.targetSdkVersion)

        applicationId = "com.showmax.sample.testy"
        versionCode  = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(libs.kotlinStdlib)
    implementation(libs.appcompat)
}
