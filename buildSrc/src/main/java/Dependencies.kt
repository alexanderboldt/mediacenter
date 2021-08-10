object Config {
    const val applicationId = "com.alex.mediacenter"
    const val minSdk = 21
    const val sdk = 30
    const val code = 1
    const val name = "1.0"
}

object Deps {
    object Kotlin {
        const val version = "1.5.10"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.3.1"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"
        const val material = "com.google.android.material:material:1.2.1"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.1"
        const val fragmentsKtx = "androidx.fragment:fragment-ktx:1.2.5"

        object LifeCycle {
            private const val version = "2.3.0"

            // viewModelScope for coroutines
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }
    }

    object Test {
        const val junit = "androidx.test.ext:junit:1.1.2"
    }

    object Libs {
        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.14.2"

        const val timber = "com.jakewharton.timber:timber:4.7.1"

        const val coil = "io.coil-kt:coil:1.0.0"

        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.3"

        object Koin {
            private const val version = "3.1.2"
            const val koin = "io.insert-koin:koin-android:$version"
        }
    }
}