object Config {
    const val applicationId = "com.alex.mediacenter"
    const val minSdk = 23
    const val sdk = 30
    const val code = 1
    const val name = "1.0"
}

object Deps {
    object Kotlin {
        const val version = "1.5.30"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val material = "com.google.android.material:material:1.4.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.0"
        const val fragmentsKtx = "androidx.fragment:fragment-ktx:1.3.6"

        object LifeCycle {
            private const val version = "2.3.1"

            // viewModelScope for coroutines
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }
    }

    object Test {
        const val junit = "androidx.test.ext:junit:1.1.2"
    }

    object Libs {
        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.15.0"

        const val timber = "com.jakewharton.timber:timber:5.0.1"

        const val coil = "io.coil-kt:coil:1.3.2"

        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"

        object Koin {
            private const val version = "3.1.2"
            const val koin = "io.insert-koin:koin-android:$version"
        }
    }
}