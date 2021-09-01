package com.alex.mediacenter.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.feature.dummy.DummyScreen
import com.alex.mediacenter.feature.player.PlayerScreen

@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
            )
            BottomSheetScaffold(
                sheetContent = { PlayerScreen(bottomSheetScaffoldState) },
                scaffoldState = bottomSheetScaffoldState,
                sheetPeekHeight = 100.dp) {
                DummyScreen()
            }
        }
    }
}