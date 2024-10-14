package com.macpry.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomNoteDao : NoteDao {

    @Insert
    suspend fun insert(note: DbNote)
    override suspend fun insert(note: Note) = insert(DbNote(content = note.content))

    @Query("SELECT * FROM dbNote")
    override fun getAll(): Flow<List<Note>>

}
