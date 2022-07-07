package com.alex.mediacenter.feature.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alex.mediacenter.R
import com.alex.mediacenter.ui.theme.*
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun PlayerScreen(bottomSheetState: BottomSheetScaffoldState, peekHeight: Dp, viewModel: PlayerViewModel = getViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {

        // todo: make the image blurred
        AsyncImage(
            model = viewModel.state.playerPreview.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(MineShaft),
            contentScale = ContentScale.Crop
        )

        Column {
            SmallPlayer(bottomSheetState, peekHeight)
            BigPlayer()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SmallPlayer(bottomSheetState: BottomSheetScaffoldState, peekHeight: Dp, viewModel: PlayerViewModel = getViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(peekHeight)
    ) {
        LinearProgressIndicator(
            viewModel.state.playerPreview.progress / viewModel.state.playerPreview.duration,
            modifier = Modifier.fillMaxWidth()
        )

        /*
        var sliderPosition by remember { mutableStateOf(0f) }
        var isSliderInteracting by remember { mutableStateOf(false) }
        Slider(
            value = if (isSliderInteracting) sliderPosition else viewModel.state.playerPreview.progress,
            valueRange = 0f..viewModel.state.playerPreview.duration,
            onValueChange = {
                isSliderInteracting = true
                sliderPosition = it
            },
            onValueChangeFinished = {
                viewModel.onSeek(sliderPosition)
                isSliderInteracting = false
            }
        )
         */

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = viewModel.state.playerPreview.coverUrl,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = viewModel.state.playerPreview.title,
                modifier = Modifier.weight(1f),
                color = White
            )

            Spacer(Modifier.width(8.dp))

            /*
            IconButton(
                onClick = { viewModel.onClickPrevious() },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(75.dp)
            ) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_media_previous),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = White
                )
            }
             */

            when (viewModel.state.playerPreview.showPlayButton) {
                true -> PlayPauseButton(true) { viewModel.onClickPlay() }
                false -> PlayPauseButton(false) { viewModel.onClickPause() }
            }

            /*
            IconButton(
                onClick = { viewModel.onClickNext() },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(75.dp)
            ) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_media_next),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = White
                )
            }
             */
        }
    }
}

@Composable
fun BigPlayer(viewModel: PlayerViewModel = getViewModel()) {
    Column(Modifier.padding(16.dp)) {
        AsyncImage(
            model = viewModel.state.playerPreview.coverUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(8.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = viewModel.state.playerPreview.positionFormatted, color = White)
            Text(text = viewModel.state.playerPreview.durationFormatted, color = White)
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