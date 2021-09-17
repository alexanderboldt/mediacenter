package com.alex.mediacenter.feature.dummy

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.R
import org.koin.androidx.compose.getViewModel

@Composable
fun DummyScreen() {
    val viewModel: DummyViewModel = getViewModel()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { viewModel.onClickReleasePlayer() },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .height(75.dp)
                .fillMaxWidth()) {

            Text(text = stringResource(id = R.string.dummy_release_player))
        }

        Button(
            onClick = { viewModel.onClickButtonOne() },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(75.dp)
                .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.dummy_lateline))
        }

        Button(
            onClick = { viewModel.onClickButtonTwo() },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(75.dp)
                .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.dummy_blue_moon))
        }
    }
}