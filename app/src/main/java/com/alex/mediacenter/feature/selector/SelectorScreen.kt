@file:OptIn(ExperimentalFoundationApi::class)

package com.alex.mediacenter.feature.selector

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alex.mediacenter.R
import com.alex.mediacenter.feature.selector.model.State
import com.alex.mediacenter.ui.theme.MineShaft
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun SelectorScreen(bottomSheetState: BottomSheetScaffoldState, viewModel: SelectorViewModel = getViewModel()) {
    BackHandler(bottomSheetState.bottomSheetState.isCollapsed) {
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
        LazyColumn(state = listState) {
            itemsIndexed(
                items = directoriesAndFiles.items,
                key = { _, item -> item.hashCode() }
            ) { index, item ->
                if (index != 0) Divider(modifier = Modifier.padding(horizontal = 8.dp))

                when (item) {
                    is State.DirectoryOrFileBase.Directory -> {
                        DirectoryOrFile(
                            onLongClick = {},
                            onClick = { viewModel.onClickDirectory(item) },
                            isDirectory = true,
                            name = item.name
                        )
                    }
                    is State.DirectoryOrFileBase.File -> {
                        DirectoryOrFile(
                            onLongClick = { viewModel.onLongClickFile(item) },
                            onClick = { viewModel.onClickFile(item) },
                            isDirectory = false,
                            name = item.name
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DirectoryOrFile(onLongClick: () -> Unit, onClick: () -> Unit, isDirectory: Boolean, name: String) {
    Row(modifier = Modifier
        .combinedClickable(onLongClick = onLongClick, onClick = onClick)
        .padding(24.dp)
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