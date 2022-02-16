plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
//    id("dagger.hilt.android.plugin")
}

android {

    compileSdk = Versions.COMPILE_SDK
    defaultConfig {

        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK

        buildConfigField(
            "String",
            "BOOTSTRAP_CONF_DATA_FILENAME", project.properties["bootstrap_conference_data_filename"] as String
        )
    }
}

dependencies {

    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))
    androidTestApi(platform(project(":depconstraints")))

    implementation(project(":model"))

    // Utils
    implementation(Libs.GSON)

    // DataStore
    implementation(Libs.DATA_STORE_PREFERENCES)

    // Kotlin
    implementation(Libs.KOTLIN_STDLIB)

    // Coroutines
    api(Libs.COROUTINES)

    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    kapt(Libs.HILT_COMPILER)

}
