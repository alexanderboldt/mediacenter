plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}
apply {
    plugin("kotlin-android")
}

android {
    compileSdkVersion(Config.sdk)
    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.sdk)
        versionCode = Config.code
        versionName = Config.name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        ndk?.abiFilters("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false

            applicationIdSuffix = ".development"

            resValue("string", "app_name", "mediacenter (debug)")
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

            resValue("string", "app_name", "newstime")
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    buildFeatures {
        viewBinding = true
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
    jcenter()
    maven { setUrl("https://www.jitpack.io") }
}

dependencies {

    // testing
    testImplementation(Deps.Test.junit)

    // android-testing
    androidTestImplementation(Deps.Test.junit)

    // androidx
    implementation(Deps.AndroidX.core)
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.constraintLayout)

    implementation(Deps.AndroidX.lifecycleRuntime)
    implementation(Deps.AndroidX.lifecycleExtensions)
    implementation(Deps.AndroidX.lifecycleCommonJava)
    kapt(Deps.AndroidX.lifecycleCompiler)

    implementation(Deps.AndroidX.fragmentsKtx)

    // 3rd-party libraries

    // audio
    implementation(Deps.Libs.exoplayer)

    // logging
    implementation(Deps.Libs.timber)

    // image
    implementation(Deps.Libs.coil)

    // leak-detection
    debugImplementation(Deps.Libs.leakCanary)

    // dependency injection
    implementation(Deps.Libs.Koin.koin)
    implementation(Deps.Libs.Koin.viewModel)

    implementation(Deps.AndroidX.LifeCycle.viewModelKtx)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")

}