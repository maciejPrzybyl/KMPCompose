package com.macpry.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Upsert

//TODO Find way to use base
interface BaseRoomDao<T>: BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(list: List<T>)

    @Update
    override suspend fun update(obj: T): Int

    @Upsert
    override suspend fun upsert(obj: T): Long

    @Delete
    override suspend fun delete(obj: T): Int

    @Delete
    override suspend fun delete(list: List<T>)
}
