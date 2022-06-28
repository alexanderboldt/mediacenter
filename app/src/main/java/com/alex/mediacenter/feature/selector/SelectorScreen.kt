package com.alex.mediacenter.feature.selector

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.feature.selector.model.UiModelBase
import org.koin.androidx.compose.getViewModel
import java.io.File

@Composable
fun SelectorScreen(viewModel: SelectorViewModel = getViewModel()) {
    BackHandler {
        viewModel.onClickBack()
    }

    BoxWithConstraints {
        Column(
            modifier = Modifier
                .height(maxHeight - 100.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            viewModel.directoryState.forEach { fileOrDirectory ->
                when (fileOrDirectory) {
                    is UiModelBase.UiModelDirectory -> {
                        Button(onClick = { viewModel.onClickDirectory(fileOrDirectory) }) {
                            Text(text = fileOrDirectory.name)
                        }
                    }
                    is UiModelBase.UiModelFile -> {
                        Button(onClick = { viewModel.onClickFile(fileOrDirectory) }) {
                            Text(text = fileOrDirectory.name)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        FloatingActionButton(
            onClick = { viewModel.onClickPlayFab() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_play),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}