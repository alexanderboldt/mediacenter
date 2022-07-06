package com.alex.mediacenter.feature.selector.model

sealed interface UiModelContent {
    object Items : UiModelContent
    object Empty : UiModelContent
}