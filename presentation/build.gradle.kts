plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK

        buildConfigField(
            "com.google.android.gms.maps.model.LatLng",
            "MAP_VIEWPORT_BOUND_NE",
            "new com.google.android.gms.maps.model.LatLng(${project.properties["map_viewport_bound_ne"]})"
        )
        buildConfigField(
            "com.google.android.gms.maps.model.LatLng",
            "MAP_VIEWPORT_BOUND_SW",
            "new com.google.android.gms.maps.model.LatLng(${project.properties["map_viewport_bound_sw"]})"
        )

        buildConfigField("float", "MAP_CAMERA_FOCUS_ZOOM", project.properties["map_camera_focus_zoom"] as String)

        resValue("dimen", "map_camera_bearing", project.properties["map_default_camera_bearing"] as String)
        resValue("dimen", "map_camera_target_lat", project.properties["map_default_camera_target_lat"] as String)
        resValue("dimen", "map_camera_target_lng", project.properties["map_default_camera_target_lng"] as String)
        resValue("dimen", "map_camera_tilt", project.properties["map_default_camera_tilt"] as String)
        resValue("dimen", "map_camera_zoom", project.properties["map_default_camera_zoom"] as String)
        resValue("dimen", "map_viewport_min_zoom", project.properties["map_viewport_min_zoom"] as String)
        resValue("dimen", "map_viewport_max_zoom", project.properties["map_viewport_max_zoom"] as String)

    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}
dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))
    androidTestApi(platform(project(":depconstraints")))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation("androidx.fragment:fragment-ktx:1.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")


    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}