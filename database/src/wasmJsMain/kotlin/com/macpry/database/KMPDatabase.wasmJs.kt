package com.macpry.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module

actual val databaseModule
    get() = module {
        single<KMPDatabase> { WasmDatabase() }
    }

class WasmDatabase : KMPDatabase {

    init {
        initSqlJsDatabase()
    }

    private fun initSqlJsDatabase() {
        createDatabase()//.await<Any>()
    }

    override fun noteDao() = object : NoteDao {
        override suspend fun insert(note: Note) {
            //db.exec("INSERT INTO Item (id, content) VALUES (${item.id}, '${note.content}')")
            //db.exec("INSERT INTO DbNote (id, content) VALUES (1, '${note.content}')")
        }

        override fun getAll(): Flow<List<Note>> = flow {
            /*val result = db.exec("SELECT * FROM DbNote")
            //val resultArray = (result as Array<Array<JsAny>>)[0]
            //val values = resultArray as Array<Array<JsAny>>
            result[0].values.map { dbNote: DbNote ->
                Note(dbNote.content)
            }*/
            listOf(
                Note("aaaaa"),
                Note("bbbbb"),
                Note("cccccc")
            )
        }

        /*override fun getAll(): Flow<List<Note>> = flowOf(
            listOf(
                Note("aaaaa"),
                Note("bbbbb"),
                Note("cccccc")
            )
        )*/
    }

    data class DbNote(
        val id: Long,
        val content: String
    )

}
