//https://developer.android.com/jetpack/compose/bom/bom?hl=ko
//https://developer.android.com/jetpack/androidx/releases/compose?hl=ko
//https://developer.android.com/jetpack/androidx/releases/compose-kotlin?hl=ko
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
