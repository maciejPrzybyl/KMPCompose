package com.macpry.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

const val KMP_DATABASE_NAME = "KMPDatabase.db"

@Database(
    entities = [DbNote::class],
    version = 1
)
@ConstructedBy(KMPDatabaseConstructor::class)
abstract class KMPDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object KMPDatabaseConstructor : RoomDatabaseConstructor<KMPDatabase> {
    override fun initialize(): KMPDatabase
}

//expect val databaseModule: Module

fun getRoomDatabase(
    builder: RoomDatabase.Builder<KMPDatabase>
    //TODO Inject IO dispatcher
): KMPDatabase {
    return builder
        //.addMigrations(MIGRATIONS)
        //.fallbackToDestructiveMigrationOnDowngrade()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default/*Dispatchers.IO*/)
        .build()
}
