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
    compileSdkVersion(Deps.Config.sdk)
    defaultConfig {
        applicationId = Deps.Config.applicationId
        minSdkVersion(Deps.Config.minSdk)
        targetSdkVersion(Deps.Config.sdk)
        versionCode = Deps.Config.code
        versionName = Deps.Config.name

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

    viewBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

repositories {
    mavenLocal()
    maven { setUrl("https://www.jitpack.io") }
    mavenCentral()
    google()
    jcenter()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {

    // testing
    testImplementation(Deps.Test.junit)
    testImplementation(Deps.Test.testRunner)

    // android-testing
    androidTestImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.mockitoCore)
    androidTestImplementation(Deps.Test.mockitoAndroid)
    androidTestImplementation(Deps.Test.coreTesting)
    androidTestImplementation(Deps.Test.testRunner)
    androidTestImplementation(Deps.Test.activityTestRule)
    androidTestImplementation(Deps.Test.espressoCore)

    // androidx
    implementation(Deps.AndroidX.appCompat)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.constraintLayout)

    implementation(Deps.AndroidX.lifecycleRuntime)
    implementation(Deps.AndroidX.lifecycleExtensions)
    implementation(Deps.AndroidX.lifecycleCommonJava)
    kapt(Deps.AndroidX.lifecycleCompiler)
    implementation(Deps.AndroidX.lifecycleViewModelKtx)

    // core-library
    implementation(Deps.Libs.androidCore)

    // 3rd-party libraries

    // audio
    implementation(Deps.Libs.exoplayer)

    // logging
    implementation(Deps.Libs.timber)

    // image
    implementation(Deps.Libs.glide)
    kapt(Deps.Libs.glideCompiler)
    implementation(Deps.Libs.glideOkHttpIntegration)
    implementation(Deps.Libs.glideTransformations)

    // fragments-alternative
    implementation(Deps.Libs.conductor)
    implementation(Deps.Libs.conductorSupport)
    implementation(Deps.Libs.conductorViewModel)

    // reactive
    implementation(Deps.Libs.rxjava)
    implementation(Deps.Libs.rxandroid)

    // view-binding
    implementation(Deps.Libs.rxbinding)

    // leak-detection
    debugImplementation(Deps.Libs.leakCanaryDebug)
}