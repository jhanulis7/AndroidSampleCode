plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.ftd.ivi.appstore.common.database"
    defaultConfig {
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "com.ftd.ivi.appstore.CustomTestRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {

    // coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // hilt : https://github.com/googlecodelabs/android-hilt
    implementation(libs.bundles.hilt)
    kapt(libs.hilt.android.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

//    testImplementation(libs.hilt.android.testing)
//    kaptTest(libs.hilt.android.compiler)
//    androidTestImplementation(libs.hilt.android.testing)
//    kaptAndroidTest(libs.hilt.android.compiler)

    // Room "androidx-room-compiler",
    implementation(libs.bundles.room)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)

//
    // Testing dependencies
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.test.ext.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
//    testImplementation(libs.robolectric)
//    testAnnotationProcessor(libs.hilt.android.compiler)
//    androidTestAnnotationProcessor(libs.hilt.android.compiler)
}