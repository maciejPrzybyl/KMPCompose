@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

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
abstract class KMPRoomDatabase : RoomDatabase(), KMPDatabase {
    abstract override fun noteDao(): RoomNoteDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object KMPDatabaseConstructor : RoomDatabaseConstructor<KMPRoomDatabase> {
    override fun initialize(): KMPRoomDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<KMPRoomDatabase>
    //TODO Inject IO dispatcher
): KMPRoomDatabase {
    return builder
        //.addMigrations(MIGRATIONS)
        //.fallbackToDestructiveMigrationOnDowngrade()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default/*Dispatchers.IO*/)
        .build()
}
