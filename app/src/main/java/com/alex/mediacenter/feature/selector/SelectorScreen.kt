package com.alex.mediacenter.feature.selector

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.R
import com.alex.mediacenter.feature.selector.model.State
import com.alex.mediacenter.ui.theme.MineShaft
import org.koin.androidx.compose.getViewModel

@Composable
fun SelectorScreen(viewModel: SelectorViewModel = getViewModel()) {
    BackHandler {
        viewModel.onClickBack()
    }

    when (val state = viewModel.state.content) {
        is State.Content.DirectoriesAndFiles -> ItemsScreen(state)
        is State.Content.Empty -> EmptyScreen()
    }
}

// ----------------------------------------------------------------------------

@Composable
fun ItemsScreen(directoriesAndFiles: State.Content.DirectoriesAndFiles) {
    val viewModel: SelectorViewModel = getViewModel()

    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = directoriesAndFiles.items,
                key = { it.hashCode() }
            ) { item ->
                when (item) {
                    is State.DirectoryOrFileBase.Directory -> {
                        DirectoryOrFile(
                            onClick = { viewModel.onClickDirectory(item) },
                            isDirectory = true,
                            name = item.name
                        )
                    }
                    is State.DirectoryOrFileBase.File -> {
                        DirectoryOrFile(
                            onClick = { viewModel.onClickFile(item) },
                            isDirectory = false,
                            name = item.name
                        )
                    }
                }
            }
        }

        if (viewModel.state.isFabVisible) {
            FloatingActionButton(
                onClick = { viewModel.onClickPlayFab() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun DirectoryOrFile(onClick: () -> Unit, isDirectory: Boolean, name: String) {
    Row(modifier = Modifier
        .clickable(onClick = onClick)
        .border(1.dp, MineShaft)
        .padding(16.dp)
        .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = if (isDirectory) R.drawable.ic_folder else R.drawable.ic_audio_file),
            contentDescription = null,
            tint = MineShaft
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name)
    }
}

// ----------------------------------------------------------------------------

@Composable
fun EmptyScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No suitable content available")
    }
}