package com.alex.mediacenter.feature.dummy

import androidx.annotation.StringRes
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
fun DummyScreen(viewModel: DummyViewModel = getViewModel()) {
    Column(modifier = Modifier.padding(16.dp)) {
        DummyButton(onClick = { viewModel.onClickReleasePlayer() }, string = R.string.dummy_release_player)
        DummyButton(onClick = { viewModel.onClickButtonOne() }, string = R.string.dummy_lateline)
        DummyButton(onClick = { viewModel.onClickButtonTwo() }, string = R.string.dummy_blue_moon)
    }
}

@Composable
fun DummyButton(onClick: () -> Unit, @StringRes string: Int) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .height(75.dp)
            .fillMaxWidth()) {
        Text(text = stringResource(id = string))
    }
}