object Config {
    const val applicationId = "com.alex.mediacenter"
    const val minSdk = 23
    const val sdk = 31
    const val code = 1
    const val name = "1.0"
}

object Deps {
    object Kotlin {
        const val version = "1.5.31"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val material = "com.google.android.material:material:1.4.0"
        const val splashScreen = "androidx.core:core-splashscreen:1.0.0-alpha01"

        object Compose {
            const val version = "1.0.4"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val material = "androidx.compose.material:material:$version"
        }

        object LifeCycle {
            private const val version = "2.3.1"

            // viewModelScope for coroutines
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        object Navigation {
            const val compose = "androidx.navigation:navigation-compose:2.4.0-alpha04"
        }
    }

    object Test {
        const val junit = "androidx.test.ext:junit:1.1.2"
    }

    object Libs {
        object Accompanist {
            private val version = "v0.20.0"
            val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:$version>"
        }

        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.15.1"

        const val timber = "com.jakewharton.timber:timber:5.0.1"

        object Coil {
            private const val version = "1.3.2"
            const val compose = "io.coil-kt:coil-compose:$version"
        }

        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"

        object Koin {
            private const val version = "3.1.2"
            const val koin = "io.insert-koin:koin-android:$version"
            const val compose = "io.insert-koin:koin-androidx-compose:$version"
        }
    }
}