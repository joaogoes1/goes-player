import com.android.utils.TraceUtils.simpleId

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.goesplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.goesplayer"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
            compose = true
    }
    useLibrary("org.apache.http.legacy")
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)

    ksp(libs.androidx.room.compiler)
    ksp(libs.hilt.android.compiler)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}
