package com.alex.mediacenter.feature.selector

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alex.mediacenter.feature.selector.model.UiModelBase
import com.alex.mediacenter.player.MediaPlayer
import java.io.File

class SelectorViewModel(private val mediaPlayer: MediaPlayer) : ViewModel() {

    var directoryState: List<UiModelBase> by mutableStateOf(emptyList())
        private set

    private var currentDirectory = File("/sdcard/")
    private val currentDirectoriesOrFiles: List<File>?
        get() {
            return currentDirectory
                .listFiles()
                ?.toList()
                ?.filterNot { it.isHidden }
        }

    // ----------------------------------------------------------------------------

    init {
        updateDirectoryState()
    }

    // ----------------------------------------------------------------------------

    fun onClickBack() {
        currentDirectory.parentFile?.also {
            if (it.path == "/") return
            currentDirectory = it
            updateDirectoryState()
        }
    }

    fun onClickDirectory(directory: UiModelBase.UiModelDirectory) {
        currentDirectory = File(directory.path)
        updateDirectoryState()
    }

    fun onClickFile(file: UiModelBase.UiModelFile) {
        mediaPlayer.play(listOf(file.path))
    }

    fun onClickPlayFab() {
        currentDirectoriesOrFiles
            ?.filter { it.isFile }
            ?.map { it.path }
            ?.also { mediaPlayer.play(it) }
    }

    // ----------------------------------------------------------------------------

    private fun updateDirectoryState() {
        directoryState = currentDirectoriesOrFiles
            ?.map {
                when (it.isDirectory) {
                    true -> UiModelBase.UiModelDirectory(it.name, it.path)
                    false -> UiModelBase.UiModelFile(it.name, it.path)
                }
            }?.sortedBy {
                when (it) {
                    is UiModelBase.UiModelDirectory -> it.name
                    is UiModelBase.UiModelFile -> it.name
                }
            } ?: emptyList()
    }
}