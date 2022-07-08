package com.alex.mediacenter.feature.selector

import android.Manifest
import androidx.lifecycle.viewModelScope
import com.alex.mediacenter.feature.base.BaseViewModel
import com.alex.mediacenter.feature.selector.model.State
import com.alex.mediacenter.player.MediaPlayer
import com.alexstyl.warden.Warden
import kotlinx.coroutines.launch
import java.io.File

class SelectorViewModel(private val mediaPlayer: MediaPlayer, private val warden: Warden) : BaseViewModel<State, Unit>() {

    private val supportedExtensions = listOf("mp3", "m4a")

    private var currentDirectory = File("/sdcard/")
    private val currentDirectoriesOrFiles: List<File>?
        get() {
            return currentDirectory
                .listFiles()
                ?.toList()
                ?.filterNot { it.isHidden }
                ?.filter { it.isDirectory || it.extension in supportedExtensions }
                ?.sortedWith(compareBy({ it.isFile }, { it.name }))
        }

    override val state = State()

    // ----------------------------------------------------------------------------

    init {
        viewModelScope.launch {
            warden.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            updateDirectoryState()
        }
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
        val paths = currentDirectoriesOrFiles!!.filter { it.isFile }.map { it.path }
        val startIndex = paths.indexOf(file.path)

        mediaPlayer.play(paths, startIndex)
    }

    // ----------------------------------------------------------------------------

    private fun updateDirectoryState() {
        state.content = currentDirectoriesOrFiles
            ?.map {
                when (it.isDirectory) {
                    true -> State.DirectoryOrFileBase.Directory(it.name, it.path)
                    false -> State.DirectoryOrFileBase.File(it.name, it.path)
                }
            }?.takeIf { it.isNotEmpty() }
            ?.let { State.Content.DirectoriesAndFiles(it) }
            ?: State.Content.Empty
    }
}