plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id ("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "com.oscorella.cafeyn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oscorella.cafeyn"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"https://b2c-api.cafeyn.co/\"")
        }
        release {
            buildConfigField("String", "API_BASE_URL", "\"https://b2c-api.cafeyn.co/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    //
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    // DI
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation (libs.androidx.lifecycle.process)
    kapt(libs.hilt.android.compiler)
    //Network
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    //DB
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation (libs.androidx.room.ktx)
    implementation (libs.kotlinx.serialization.json)

    //Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation (libs.mockwebserver)
    testImplementation (libs.mockito.core)
}