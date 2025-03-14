buildscript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.54")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    kotlin("jvm") version "1.4.21" apply false
    kotlin("plugin.serialization") version "2.0.0" apply false
}