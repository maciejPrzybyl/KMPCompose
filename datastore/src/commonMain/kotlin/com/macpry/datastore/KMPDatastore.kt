package com.macpry.datastore

import kotlinx.coroutines.flow.Flow
import org.koin.core.module.Module

expect val datastoreModule: Module

interface KMPDatastore {
    val selectedNumber: Flow<Int>
    suspend fun setNumber(value: Int)
}

object KMPDatastoreKeys {
    const val SELECTED_NUMBER = "SELECTED_NUMBER"
}
