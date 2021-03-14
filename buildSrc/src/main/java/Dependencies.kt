object Config {
    const val applicationId = "com.alex.mediacenter"
    const val minSdk = 21
    const val sdk = 30
    const val code = 1
    const val name = "1.0"
}

object Deps {
    object Kotlin {
        const val version = "1.4.31"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.3.1"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"
        const val material = "com.google.android.material:material:1.2.1"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.1"
        const val fragmentsKtx = "androidx.fragment:fragment-ktx:1.2.5"

        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:2.2.0"
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
        const val lifecycleCommonJava = "androidx.lifecycle:lifecycle-common-java8:2.2.0"
        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:2.2.0"

        object LifeCycle {
            private const val version = "2.3.0"

            // coroutineScope() on Lifecycle, lifecycleScope on LifecycleOwner
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"

            // Annotation processor
            const val compiler = "androidx.lifecycle:lifecycle-compiler:$version"

            // viewModelScope for coroutines
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        object Navigation {
            private const val version = "2.3.0"
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Room {
            private const val version = "2.2.5"
            const val room = "androidx.room:room-runtime:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
        }

        object DataStore {
            private const val version = "1.0.0-alpha02"
            const val preferences = "androidx.datastore:datastore-preferences:$version"
        }
    }

    object Test {
        const val junit = "androidx.test.ext:junit:1.1.2"
    }

    object Libs {
        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.13.2"

        object Coroutines {
            private const val version = "1.3.9"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }

        const val timber = "com.jakewharton.timber:timber:4.7.1"

        object Moshi {
            private const val version = "1.10.0"
            const val moshi = "com.squareup.moshi:moshi:$version"
            const val codeGen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
        }

        const val coil = "io.coil-kt:coil:1.0.0"

        const val liveEvent = "com.github.hadilq.liveevent:liveevent:1.2.0"

        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.3"

        object Koin {
            private const val version = "2.1.6"
            const val koin = "org.koin:koin-android:$version"
            const val viewModel = "org.koin:koin-androidx-viewmodel:$version"
        }

        object Corbind {
            private const val version = "1.4.0"
            const val corbind = "ru.ldralighieri.corbind:corbind-core:$version"
            const val appCompat = "ru.ldralighieri.corbind:corbind-appcompat:$version"
        }
    }
}