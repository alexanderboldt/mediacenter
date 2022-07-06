package com.alex.mediacenter.feature.selector

import com.alex.mediacenter.feature.base.BaseViewModel
import com.alex.mediacenter.feature.selector.model.State
import com.alex.mediacenter.player.MediaPlayer
import java.io.File

class SelectorViewModel(private val mediaPlayer: MediaPlayer) : BaseViewModel<State, Unit>() {

    private val supportedExtensions = listOf("mp3", "m4a")

    private var currentDirectory = File("/sdcard/")
    private val currentDirectoriesOrFiles: List<File>?
        get() {
            return currentDirectory
                .listFiles()
                ?.toList()
                ?.filterNot { it.isHidden }
                ?.filter { it.isDirectory || it.extension in supportedExtensions }
        }

    override val state = State()

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

    fun onClickDirectory(directory: State.DirectoryOrFileBase.Directory) {
        currentDirectory = File(directory.path)
        updateDirectoryState()
    }

    fun onClickFile(file: State.DirectoryOrFileBase.File) {
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
        val directoriesOrFilesPrepared = currentDirectoriesOrFiles
            ?.map {
                when (it.isDirectory) {
                    true -> State.DirectoryOrFileBase.Directory(it.name, it.path)
                    false -> State.DirectoryOrFileBase.File(it.name, it.path)
                }
            }?.sortedBy {
                when (it) {
                    is State.DirectoryOrFileBase.Directory -> it.name
                    is State.DirectoryOrFileBase.File -> it.name
                }
            } ?: emptyList()

        state.content = when (directoriesOrFilesPrepared.isNotEmpty()) {
            true -> State.Content.DirectoriesAndFiles(directoriesOrFilesPrepared)
            false -> State.Content.Empty
        }

        state.isFabVisible = currentDirectoriesOrFiles?.any { it.isFile } ?: false
    }
}