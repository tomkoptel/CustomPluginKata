package com.sample.coolplugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class CoolPluginSpec extends Specification {
    @Rule TemporaryFolder temporaryFolder = new TemporaryFolder()
    def COMPILE_SDK_VERSION = 27
    def BUILD_TOOLS_VERSION = "27.0.1"
    def APPLICATION_ID = "com.sample.coolplugin.integration"
    def MANIFEST_FILE_TEXT = """<?xml version="1.0" encoding="utf-8"?>
      <manifest package="com.sample.coolplugin.integration"
                xmlns:android="http://schemas.android.com/apk/res/android">
          <application/>
      </manifest>
  """

    Project project
    File manifestFile

    def "setup"() {
        project = ProjectBuilder.builder().build()
        manifestFile = new File(project.projectDir, "src/main/AndroidManifest.xml")
        manifestFile.parentFile.mkdirs()
        manifestFile.write(MANIFEST_FILE_TEXT)
    }

    @Unroll "should not conflict with #projectPlugin"() {
        given:
        project.apply plugin: projectPlugin

        when:
        project.apply plugin: "com.sample.coolplugin"

        then:
        noExceptionThrown()

        where:
        projectPlugin << ["com.android.application", "com.android.library", "com.android.test"]
    }

    def "should fail if android plugin missing"() {
        when:
        new CoolPlugin().apply(project)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Android plugins are missing can not configure module"
    }

    def "should create debug task only"() {
        given:
        project.apply plugin: "com.android.application"
        project.apply plugin: "com.sample.coolplugin"
        project.android {
            compileSdkVersion COMPILE_SDK_VERSION
            buildToolsVersion BUILD_TOOLS_VERSION

            defaultConfig {
                applicationId APPLICATION_ID
            }
        }

        when:
        project.evaluate()

        then:
        project.tasks.getByName("coolDebugAndroidTest")
    }

    def "should create task for 'custom' flavor"() {
        given:
        project.apply plugin: "com.android.application"
        project.apply plugin: "com.sample.coolplugin"
        project.android {
            compileSdkVersion COMPILE_SDK_VERSION
            buildToolsVersion BUILD_TOOLS_VERSION

            defaultConfig {
                applicationId APPLICATION_ID
            }

            buildTypes {
                debug {}
                release {}
            }

            flavorDimensions "flav"

            productFlavors {
                flavor1 { dimension "flav" }
                flavor2 { dimension "flav" }
            }
        }

        when:
        project.evaluate()

        then:
        project.tasks.getByName("coolFlavor1DebugAndroidTest")
        project.tasks.getByName("coolFlavor2DebugAndroidTest")
    }

    def "should create tasks for flavorDimensions"() {
        given:
        project.apply plugin: "com.android.application"
        project.apply plugin: "com.sample.coolplugin"
        project.android {
            compileSdkVersion COMPILE_SDK_VERSION
            buildToolsVersion BUILD_TOOLS_VERSION

            defaultConfig {
                applicationId APPLICATION_ID
            }

            buildTypes {
                debug {}
                release {}
            }

            flavorDimensions "a", "b"

            productFlavors {
                flavor1 { dimension "a" }
                flavor2 { dimension "a" }
                flavor3 { dimension "b" }
                flavor4 { dimension "b" }
            }
        }

        when:
        project.evaluate()

        then:
        project.tasks.getByName("coolFlavor1Flavor3DebugAndroidTest")
        project.tasks.getByName("coolFlavor2Flavor4DebugAndroidTest")
    }
}