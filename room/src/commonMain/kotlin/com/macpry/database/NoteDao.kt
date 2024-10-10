package com.macpry.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao: BaseDao<DbNote> {

    @Query("SELECT * FROM dbNote")
    fun getAll(): Flow<List<DbNote>>

}
