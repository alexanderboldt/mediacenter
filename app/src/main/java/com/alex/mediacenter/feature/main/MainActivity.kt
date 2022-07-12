package com.alex.mediacenter.feature.main

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.feature.player.PlayerScreen
import com.alex.mediacenter.feature.selector.SelectorScreen
import com.alex.mediacenter.receiver.HeadsetReceiver
import com.alex.mediacenter.ui.theme.*
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    private val peekHeight = 75.dp

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HeadsetReceiver.register(this)

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
                        sheetContent = { PlayerScreen(bottomSheetScaffoldState, peekHeight) },
                        scaffoldState = bottomSheetScaffoldState,
                        sheetPeekHeight = peekHeight
                    ) {
                        Box(modifier = Modifier.padding(bottom = peekHeight)) {
                            SelectorScreen()
                        }
                    }
                }
            }
        }
    }
}