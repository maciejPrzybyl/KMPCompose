package com.macpry.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

const val KMP_DATABASE_NAME = "KMPDatabase.db"

/*expect abstract class KMPDatabase: RoomDatabase {
    abstract fun noteDao(): NoteDao
}*/


@Database(
    entities = [DbNote::class],
    version = 1
)
@ConstructedBy(KMPDatabaseConstructor::class)
abstract class KMPDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object KMPDatabaseConstructor : RoomDatabaseConstructor<KMPDatabase> {
    override fun initialize(): KMPDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<KMPDatabase>
): KMPDatabase {
    return builder
        //.addMigrations(MIGRATIONS)
        //.fallbackToDestructiveMigrationOnDowngrade()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}
