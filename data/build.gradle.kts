plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
//    id("dagger.hilt.android.plugin")
}

android {
}

dependencies {

    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))
    androidTestApi(platform(project(":depconstraints")))

    implementation(project(":model"))

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
