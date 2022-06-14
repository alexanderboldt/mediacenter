package com.alex.mediacenter.feature.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alex.mediacenter.R
import com.alex.mediacenter.ui.theme.*
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun PlayerScreen(bottomSheetState: BottomSheetScaffoldState, viewModel: PlayerViewModel = getViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {

        // todo: make the image blurred
        AsyncImage(
            model = viewModel.playerPreviewState.coverUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize().background(MineShaft),
            contentScale = ContentScale.Crop
        )

        Column {
            SmallPlayer(bottomSheetState)
            BigPlayer()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SmallPlayer(bottomSheetState: BottomSheetScaffoldState, viewModel: PlayerViewModel = getViewModel()) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)) {

        LinearProgressIndicator(
            viewModel.playerPreviewState.progress,
            modifier = Modifier.fillMaxWidth(),
            color = White,
            backgroundColor = Color(0x33ffffff)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            when (viewModel.playerPreviewState.showPlayButton) {
                true -> PlayPauseButton(true) { viewModel.onClickPlay() }
                false -> PlayPauseButton(false) { viewModel.onClickPause() }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = viewModel.playerPreviewState.title,
                modifier = Modifier.weight(1f),
                color = White)

            Spacer(modifier = Modifier.size(8.dp))

            AsyncImage(
                model = viewModel.playerPreviewState.coverUrl,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun BigPlayer(viewModel: PlayerViewModel = getViewModel()) {
    Column(Modifier.padding(16.dp)) {
        AsyncImage(
            model = viewModel.playerPreviewState.coverUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(8.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = viewModel.playerPreviewState.position, color = White)
            Text(text = viewModel.playerPreviewState.duration, color = White)
        }
    }
}

@Composable
fun PlayPauseButton(isPlayButton: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxHeight()
            .width(75.dp)
    ) {
        Icon(
            painter = painterResource(if (isPlayButton) R.drawable.ic_play else R.drawable.ic_pause),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = White
        )
    }
}