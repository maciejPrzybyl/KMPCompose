package org.macpry.kmpcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class AppViewModel : ViewModel() {



    fun startCounting() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                runCatching {

                }
            }
        }
    }
}