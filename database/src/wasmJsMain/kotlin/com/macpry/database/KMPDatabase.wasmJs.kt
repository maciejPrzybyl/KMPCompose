package com.macpry.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.dsl.module

actual val databaseModule
    get() = module {
        single<KMPDatabase> { WasmDatabase() }
    }

class WasmDatabase : KMPDatabase {
    override fun noteDao() = object : NoteDao {
        override suspend fun insert(note: Note) {
            println("$note added")
        }

        override fun getAll(): Flow<List<Note>> = flowOf(
            listOf(
                Note("aaaaa"),
                Note("bbbbb"),
                Note("cccccc")
            )
        )
    }

}
