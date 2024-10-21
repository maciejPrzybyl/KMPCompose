package com.macpry.datastore

import kotlinx.coroutines.flow.Flow
import org.koin.core.module.Module

expect val datastoreModule: Module

interface KMPDatastore {
    fun getSelectedNumber(): Flow<Int>
    suspend fun setNumber(value: Int)
}

object KMPDatastoreKeys {
    const val SELECTED_NUMBER = "SELECTED_NUMBER"
}
