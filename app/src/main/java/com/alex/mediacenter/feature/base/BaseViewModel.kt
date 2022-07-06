package com.alex.mediacenter.feature.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<STATE, SIDE_EFFECT> : ViewModel() {

    abstract val state: STATE

    private var _event = Channel<SIDE_EFFECT>(Channel.RENDEZVOUS)
    val event = _event.receiveAsFlow()

    // ----------------------------------------------------------------------------

    protected suspend fun postSideEffect(event: SIDE_EFFECT) {
        _event.send(event)
    }
}