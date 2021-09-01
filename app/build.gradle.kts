plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

apply {
    plugin("kotlin-android")
}

android {
    compileSdk = Config.sdk

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdk
        targetSdk = Config.sdk
        versionCode = Config.code
        versionName = Config.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        ndk.abiFilters.addAll(mutableSetOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false

            applicationIdSuffix = ".development"

            resValue("string", "app_name", "MediaCenter (debug)")
        }

        getByName("release") {
            isDebuggable = false

            // enables code shrinking, obfuscation and optimization
            isMinifyEnabled = true

            // enables resource shrinking, which is performed by the Android Gradle Plugin
            isShrinkResources = true

            // rules for R8
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

            // use the debug-signing-configuration as long there is no keystore
            signingConfig = signingConfigs.getByName("debug")

            resValue("string", "app_name", "MediaCenter")
        }
    }

    lint {
        isAbortOnError = false
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Deps.AndroidX.Compose.version
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

repositories {
    google()
    maven { setUrl("https://www.jitpack.io") }
    mavenCentral()
}

dependencies {

    // testing
    testImplementation(Deps.Test.junit)

    // android-testing
    androidTestImplementation(Deps.Test.junit)

    // androidx
    implementation(Deps.AndroidX.core)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.LifeCycle.viewModelKtx)

    implementation(Deps.AndroidX.Compose.ui)
    implementation(Deps.AndroidX.Compose.uiTooling)
    implementation(Deps.AndroidX.Compose.foundation)
    implementation(Deps.AndroidX.Compose.material)

    implementation(Deps.AndroidX.Navigation.compose)

    // 3rd-party libraries

    // audio
    implementation(Deps.Libs.exoplayer)

    // logging
    implementation(Deps.Libs.timber)

    // image
    implementation(Deps.Libs.Coil.compose)

    // leak-detection
    debugImplementation(Deps.Libs.leakCanary)

    // dependency injection
    implementation(Deps.Libs.Koin.koin)
    implementation(Deps.Libs.Koin.compose)

    implementation(Deps.AndroidX.splashScreen)
}