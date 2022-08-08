package com.alex.mediacenter.feature.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.R
import com.alex.mediacenter.ui.theme.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun PlayerScreen(
    bottomSheetState: BottomSheetScaffoldState,
    peekHeight: Dp,
    viewModel: PlayerViewModel = getViewModel()
) {
    val scope = rememberCoroutineScope()

    BackHandler(bottomSheetState.bottomSheetState.isExpanded) {
        scope.launch {
            bottomSheetState.bottomSheetState.collapse()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BigPlayer()

        AnimatedVisibility(
            visible = bottomSheetState.bottomSheetState.isCollapsed,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            SmallPlayer(modifier = Modifier.height(peekHeight))
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SmallPlayer(modifier: Modifier = Modifier, viewModel: PlayerViewModel = getViewModel()) {
    Column(
        modifier = modifier
            .background(MineShaft)
            .fillMaxWidth()
    ) {
        LinearProgressIndicator(
            viewModel.state.playerPreview.progress / viewModel.state.playerPreview.duration,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            Text(
                text = viewModel.state.playerPreview.title,
                modifier = Modifier.weight(1f),
                color = White
            )

            Spacer(Modifier.width(8.dp))

            when (viewModel.state.playerPreview.showPlayButton) {
                true -> PlayPauseButton(true) { viewModel.onClickPlay() }
                false -> PlayPauseButton(false) { viewModel.onClickPause() }
            }
        }
    }
}

@Composable
fun BigPlayer(viewModel: PlayerViewModel = getViewModel()) {
    Column(
        Modifier
            .background(MineShaft)
            .fillMaxSize()
            .padding(16.dp)) {
        Spacer(modifier = Modifier.size(8.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = viewModel.state.playerPreview.positionFormatted, color = White)
            Text(text = viewModel.state.playerPreview.durationFormatted, color = White)
        }

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

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { viewModel.onClickPrevious() },
                modifier = Modifier.size(75.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_previous),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = White
                )
            }

            when (viewModel.state.playerPreview.showPlayButton) {
                true -> PlayPauseButton(true) { viewModel.onClickPlay() }
                false -> PlayPauseButton(false) { viewModel.onClickPause() }
            }

            IconButton(
                onClick = { viewModel.onClickNext() },
                modifier = Modifier.size(75.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_next),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = White
                )
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier
                .size(100.dp)
                .background(Amaranth)
                .clickable { viewModel.onClickReplay() })
            Box(modifier = Modifier
                .size(100.dp)
                .background(Amaranth)
                .clickable { viewModel.onClickForward() })
        }
    }
}

@Composable
fun PlayPauseButton(isPlayButton: Boolean, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(75.dp)
    ) {
        Icon(
            painter = painterResource(if (isPlayButton) R.drawable.ic_play else R.drawable.ic_pause),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            tint = White
        )
    }
}