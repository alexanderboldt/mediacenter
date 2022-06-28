package com.alex.mediacenter.feature.selector.model

sealed class UiModelBase {
    data class UiModelDirectory(val name: String, val path: String) : UiModelBase()
    data class UiModelFile(val name: String, val path: String) : UiModelBase()
}