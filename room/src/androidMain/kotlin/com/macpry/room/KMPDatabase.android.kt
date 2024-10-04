package com.macpry.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

/*@Database(
    entities = [DbNote::class],
    version = 1
)
@ConstructedBy(KMPDatabaseConstructor::class)
actual abstract class KMPDatabase : RoomDatabase() {
    actual abstract fun noteDao(): NoteDao
}*/

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<KMPDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
