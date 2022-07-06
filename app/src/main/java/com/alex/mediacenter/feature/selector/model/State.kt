package com.alex.mediacenter.feature.selector.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class State {

    sealed interface Content {
        data class DirectoriesAndFiles(val items: List<DirectoryOrFileBase>) : Content
        object Empty : Content
    }

    sealed interface DirectoryOrFileBase {
        data class Directory(val name: String, val path: String) : DirectoryOrFileBase
        data class File(val name: String, val path: String) : DirectoryOrFileBase
    }

    // ----------------------------------------------------------------------------

    var content: Content by mutableStateOf(Content.Empty)
    var isFabVisible: Boolean by mutableStateOf(false)
}