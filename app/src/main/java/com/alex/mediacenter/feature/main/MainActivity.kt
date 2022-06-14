package com.alex.mediacenter.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.feature.dummy.DummyScreen
import com.alex.mediacenter.feature.player.PlayerScreen
import com.alex.mediacenter.ui.theme.*
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()

            SideEffect {
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