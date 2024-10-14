package com.macpry.database

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module
import java.io.File

actual val databaseModule
    get() = module {
        single<KMPDatabase> { getRoomDatabase(getDatabaseBuilder()) }
    }

fun getDatabaseBuilder(): RoomDatabase.Builder<KMPRoomDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPRoomDatabase>(
        name = dbFile.absolutePath,
    )
}
