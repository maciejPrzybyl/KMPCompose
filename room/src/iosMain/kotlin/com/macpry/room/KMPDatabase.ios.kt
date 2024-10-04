package com.macpry.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/*@Database(
    entities = [DbNote::class],
    version = 1
)
@ConstructedBy(KMPDatabaseConstructor::class)
actual abstract class KMPDatabase : RoomDatabase() {
    actual abstract fun noteDao(): NoteDao
}*/

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
