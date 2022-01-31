plugins {
    id("com.android.library")
    kotlin("android")
}

android {
}

dependencies {


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("javax.inject:javax.inject:1")


    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}