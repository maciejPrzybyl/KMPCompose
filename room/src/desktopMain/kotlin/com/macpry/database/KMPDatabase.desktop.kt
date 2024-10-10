package com.macpry.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<KMPDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPDatabase>(
        name = dbFile.absolutePath,
    )
}
