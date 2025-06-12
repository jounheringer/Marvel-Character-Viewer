import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.reringuy.marvelcharacterviewer"
    compileSdk = 35

    defaultConfig {
        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))

        applicationId = "com.reringuy.marvelcharacterviewer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        android.buildFeatures.buildConfig = true

        buildConfigField("String", "MARVEL_PUBLIC_KEY", "\"${localProperties.getProperty("MARVEL_PUBLIC_KEY")}\"")
        buildConfigField("String", "MARVEL_PRIVATE_KEY", "\"${localProperties.getProperty("MARVEL_PRIVATE_KEY")}\"")
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    Constraint layout
    implementation(libs.constraintlayout.compose)

//    Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.navigation.compose)

//    Google
    implementation(libs.google.fonts)
    implementation(libs.google.gson)

//    Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

//    OkHTTP
    implementation(libs.okhttp.logging)

//    DataStore
    implementation(libs.datastore)
}