plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
}

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))
    androidTestApi(platform(project(":depconstraints")))


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("javax.inject:javax.inject:1")

    implementation(project(":model"))

    // Architecture Components
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    implementation(Libs.LIFECYCLE_RUNTIME_KTX)
    kapt(Libs.LIFECYCLE_COMPILER)

    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    androidTestImplementation(Libs.HILT_TESTING)
    kapt(Libs.HILT_COMPILER)
    kaptAndroidTest(Libs.HILT_COMPILER)

    // Utils
    api(Libs.TIMBER)

    // Firebase
    api(Libs.FIREBASE_CONFIG)

    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}