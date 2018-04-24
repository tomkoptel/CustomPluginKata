import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.setValue
import com.sample.plugin.gradle.buildPlugins
import com.sample.plugin.gradle.androidDefaults
import com.sample.plugin.gradle.libs
import com.sample.plugin.gradle.testLibs
import com.sample.plugin.gradle.projectConfig

plugins {
    id("java-gradle-plugin")
    id("kotlin")
    id("groovy")
}

java {
    sourceCompatibility = projectConfig.javaVersion
}

group = project.property("GROUP")!!
version = project.property("VERSION_NAME")!!

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion(projectConfig.kotlinVersion)
            }
        }
    }
}

gradlePlugin {
    plugins {
        this.create("coolPlugin") {
            id = "com.sample.coolplugin"
            implementationClass = "com.sample.coolplugin.CoolPlugin"
        }
    }
}

dependencies {
    implementation(libs.kotlinStdlib)
    compileOnly(buildPlugins.anroidGradlePlugin)

    testImplementation(localGroovy())
    testImplementation(buildPlugins.anroidGradlePlugin)
    testImplementation(testLibs.junit)
    testImplementation(testLibs.spockCore) { exclude(module = "groovy-all") } // Use localGroovy()
}