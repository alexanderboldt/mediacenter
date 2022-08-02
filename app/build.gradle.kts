plugins {
    application()
    kotlin()
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
    // kotlin-std-lib
    implementation(Deps.Kotlin.stdLib)

    // androidx
    Deps.AndroidX.apply {
        implementation(core)
        implementation(material)
    }

    implementation(Deps.AndroidX.LifeCycle.viewModelKtx)

    // compose
    Deps.AndroidX.Compose.apply {
        implementation(ui)
        implementation(uiTooling)
        implementation(foundation)
        implementation(material)
    }

    // 3rd-party libraries

    // Accompanist
    Deps.Libs.Accompanist.apply {
        implementation(systemUiController)
        implementation(insets)
    }

    // navigation
    implementation(Deps.AndroidX.Navigation.compose)

    // audio
    implementation(Deps.Libs.exoplayer)

    // image
    implementation(Deps.Libs.Coil.compose)

    // dependency injection
    Deps.Libs.Koin.apply {
        implementation(koin)
        implementation(compose)
    }

    implementation(Deps.Libs.warden)
}