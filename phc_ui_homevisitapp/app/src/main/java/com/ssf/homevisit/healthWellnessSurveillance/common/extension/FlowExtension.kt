package com.ssf.homevisit.healthWellnessSurveillance.common.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

fun <T> MutableSharedFlow<T>.toSharedFlow(): SharedFlow<T> = this
fun <T> MutableStateFlow<T>.toStateFlow(): SharedFlow<T> = this

fun <T : Any, S : SharedFlow<T>> LifecycleOwner.collectSharedFlowData(
    sharedFlow: S,
    body: (T) -> Unit
) {
    lifecycleScope.launch {
        sharedFlow.flowWithLifecycle(lifecycle).collect { body(it) }
    }
}