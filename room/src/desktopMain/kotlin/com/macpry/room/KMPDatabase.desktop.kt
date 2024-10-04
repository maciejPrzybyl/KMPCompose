package com.macpry.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

/*@Database(
    entities = [DbNote::class],
    version = 1
)
@ConstructedBy(KMPDatabaseConstructor::class)
actual abstract class KMPDatabase : RoomDatabase() {
    actual abstract fun noteDao(): NoteDao
}*/

fun getDatabaseBuilder(): RoomDatabase.Builder<KMPDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPDatabase>(
        name = dbFile.absolutePath,
    )
}
