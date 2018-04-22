### The Custom Gradle Plugin Boilerplate
In the following repo, you will find the boilerplate setup for the custom
Gradle plugin that is developed with a purpose to extend [Android Gradle Plugin](https://developer.android.com/studio/releases/gradle-plugin.html)
capabilities.

## What does repo demonstrate?
* Setup of the build scripts and dependencies.
* Kotlin as a primary source language.
* Setup of preconditions to check the validity of plugins integration env.
* Creation of custom task that depends on [Android TestVariant](https://android.googlesource.com/platform/tools/build/+/master/gradle/src/main/groovy/com/android/build/gradle/api/TestVariant.java).
* Integration test by [Spock Framework](http://spockframework.org).

## How to test setup?
```
./gradlew :plugin:test --rerun-tasks
```