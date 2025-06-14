buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.8.9"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
//    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
//    id("com.google.devtools.ksp") //version "1.9.23-1.0.20" apply false
}