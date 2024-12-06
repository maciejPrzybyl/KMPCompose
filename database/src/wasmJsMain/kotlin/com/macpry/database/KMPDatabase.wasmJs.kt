package com.macpry.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module

actual val databaseModule
    get() = module {
        single<KMPDatabase> { WasmDatabase() }
    }

class WasmDatabase : KMPDatabase {

    override fun noteDao() = object : NoteDao {
        override suspend fun insert(note: Note) {
            insertNote(note.content)
        }

        override fun getAll(): Flow<List<Note>> = flow {
            listOf(
                Note("aaaaa"),
                Note("bbbbb"),
                Note("cccccc")
            )
        }
    }
}
