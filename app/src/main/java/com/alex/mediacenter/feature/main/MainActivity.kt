package com.alex.mediacenter.feature.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.alex.mediacenter.feature.dummy.DummyScreen
import com.alex.mediacenter.feature.player.PlayerScreen
import com.alex.mediacenter.ui.theme.*
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Remember a SystemUiController
            val systemUiController = rememberSystemUiController()

            SideEffect {
                // Update all of the system bar colors to be transparent, and use
                systemUiController.setSystemBarsColor(AquaDeep, false)
                systemUiController.setNavigationBarColor(AquaDeep)
            }

            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
            )

            MediaCenterTheme {
                ProvideWindowInsets {
                    BottomSheetScaffold(
                        sheetContent = { PlayerScreen(bottomSheetScaffoldState) },
                        scaffoldState = bottomSheetScaffoldState,
                        sheetPeekHeight = 100.dp) {
                        DummyScreen()
                    }
                }
            }
        }
    }
}