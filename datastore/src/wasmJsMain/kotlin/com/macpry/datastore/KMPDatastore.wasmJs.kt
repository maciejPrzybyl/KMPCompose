package com.macpry.datastore

import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import org.koin.dsl.module

actual val datastoreModule = module {
    single<KMPDatastore> { WasmKMPDatastore() }
}

class WasmKMPDatastore : KMPDatastore {

    //TODO Consider using window.addEventListener("storage") {} for tracking changes
    private val storageFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override val selectedNumber: Flow<Int>
        get() = storageFlow.onSubscription {
            emit(Unit)
        }.map {
            localStorage.getItem(KMPDatastoreKeys.SELECTED_NUMBER)?.toIntOrNull() ?: 0
        }

    override suspend fun setNumber(value: Int) {
        localStorage.setItem(KMPDatastoreKeys.SELECTED_NUMBER, value.toString())
        storageFlow.tryEmit(Unit)
    }
}
