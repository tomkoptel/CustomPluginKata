import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.setValue

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
    }

    dependencies {
        // when we import from `buildSrc` folder Gradle script fails to see the dependency.
        // For that reason we use fully qualified import to access our common setup.
        com.sample.plugin.gradle.buildPlugins.apply {
            classpath(kotlinPlugin)
            classpath(anroidGradlePlugin)
        }
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

createTask("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}