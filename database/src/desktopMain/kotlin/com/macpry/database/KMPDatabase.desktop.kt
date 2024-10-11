package com.macpry.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module
import java.io.File

actual val databaseModule
    get() = module {
        single { getRoomDatabase(getDatabaseBuilder()) }
    }

fun getDatabaseBuilder(): RoomDatabase.Builder<KMPDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPDatabase>(
        name = dbFile.absolutePath,
    )
}
