import org.gradle.plugin.use.PluginDependenciesSpec

// Plugins

fun PluginDependenciesSpec.application() { id("com.android.application") }
fun PluginDependenciesSpec.kotlin() { id("kotlin-android") }

object Config {
    const val applicationId = "com.alex.mediacenter"
    const val minSdk = 26
    const val sdk = 31
    const val code = 1
    const val name = "1.0"
}

object Deps {
    object Kotlin {
        const val version = "1.6.10"
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.7.0"
        const val material = "com.google.android.material:material:1.5.0"

        object Compose {
            const val version = "1.1.1"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val material = "androidx.compose.material:material:$version"
        }

        object LifeCycle {
            private const val version = "2.4.1"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        object Navigation {
            const val compose = "androidx.navigation:navigation-compose:2.4.0-alpha04"
        }
    }

    object Libs {
        object Accompanist {
            private const val version = "0.23.1"
            const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
            const val insets = "com.google.accompanist:accompanist-insets:$version"
        }

        object Coil {
            private const val version = "2.1.0"
            const val compose = "io.coil-kt:coil-compose:$version"
        }

        const val exoplayer = "com.google.android.exoplayer:exoplayer:2.17.1"

        object Koin {
            private const val version = "3.2.0"
            const val koin = "io.insert-koin:koin-android:$version"
            const val compose = "io.insert-koin:koin-androidx-compose:$version"
        }
    }
}