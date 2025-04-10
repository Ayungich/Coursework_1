plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.ayungi.travelapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ayungi.travelapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
    implementation(libs.retrofit)
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
    implementation(libs.converter.gson)
    // https://mvnrepository.com/artifact/com.google.android.gms/play-services-location
    implementation(libs.play.services.location)
    // https://mvnrepository.com/artifact/de.hdodenhof/circleimageview
    implementation(libs.circleimageview)
    // https://mvnrepository.com/artifact/org.osmdroid/osmdroid-android
    implementation(libs.osmdroid.android)
    implementation(libs.lifecycle.viewmodel.android)
    // https://mvnrepository.com/artifact/com.prolificinteractive/material-calendarview
    implementation(libs.material.calendarview)
    // https://mvnrepository.com/artifact/com.github.lecho/hellocharts-library
    implementation(libs.hellocharts.library)
    implementation(libs.mapbox.sdk.geojson)
    implementation(libs.mapbox.sdk.services.v732)
    implementation(libs.mapbox.sdk.turf)
    implementation(libs.mapbox.sdk.core)
    implementation(libs.annotation)
    implementation(libs.android)
    implementation(libs.core.ktx)
    implementation("com.mapbox.maps:android:11.11.0")
    // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-moshi
    runtimeOnly(libs.converter.moshi)
    // https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
    implementation(libs.glide)
    implementation(libs.mpandroidchart.vv310)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}