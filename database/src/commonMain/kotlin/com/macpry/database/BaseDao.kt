package com.macpry.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Upsert

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<T>)

    @Update
    suspend fun update(obj: T): Int

    @Upsert
    suspend fun upsert(obj: T): Long

    @Delete
    suspend fun delete(obj: T): Int

    @Delete
    suspend fun delete(list: List<T>)
}
