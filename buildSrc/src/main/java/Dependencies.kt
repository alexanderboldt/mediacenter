object Deps {
    object Config {
        val applicationId = "com.alex.mediacenter"
        val minSdk = 21
        val sdk = 30
        val code = 1
        val name = "1.0"
    }

    object AndroidX {
        val core = "androidx.core:core-ktx:1.3.0"
        val appCompat = "androidx.appcompat:appcompat:1.1.0"
        val material = "com.google.android.material:material:1.1.0"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0"

        val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:2.2.0"
        val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        val lifecycleCommonJava = "androidx.lifecycle:lifecycle-common-java8:2.2.0"
        val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:2.2.0"

        val fragmentsExt = "androidx.fragment:fragment-ktx:1.2.5"
    }

    object Test {
        val junit = "androidx.test.ext:junit:1.0.0"
        val testRunner = "androidx.test:runner:1.1.0"
        val mockitoCore = "org.mockito:mockito-core:2.6.3"
        val mockitoAndroid = "org.mockito:mockito-android:2.6.3"

        val coreTesting = "androidx.arch.core:core-testing:2.0.0-rc01"

        val activityTestRule = "androidx.test:rules:1.1.0"
        val espressoCore = "androidx.test.espresso:espresso-core:3.1.0"
    }

    object Libs {
        val androidCore = "com.github.alexanderboldt:androidcore:2.0.0"

        val exoplayer = "com.google.android.exoplayer:exoplayer:2.11.5"

        val timber = "com.jakewharton.timber:timber:4.7.1"

        val glide = "com.github.bumptech.glide:glide:4.11.0"
        val glideCompiler = "com.github.bumptech.glide:compiler:4.11.0"
        val glideOkHttpIntegration = "com.github.bumptech.glide:okhttp3-integration:4.11.0"
        val glideTransformations = "jp.wasabeef:glide-transformations:4.0.0"

        val rxbinding = "com.jakewharton.rxbinding4:rxbinding:4.0.0"

        val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.4"

        val koin = "org.koin:koin-android:2.1.6"
        val koinViewModel = "org.koin:koin-androidx-viewmodel:2.1.6"
    }
}