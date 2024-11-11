import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val databaseModule
    get() = module {
        single<KMPDatabase> { getRoomDatabase(getDatabaseBuilder()) }
    }

fun getDatabaseBuilder(): RoomDatabase.Builder<KMPRoomDatabase> {
    val dbFilePath = documentDirectory() + "/$KMP_DATABASE_NAME"
    return Room.databaseBuilder<KMPRoomDatabase>(
        name = dbFilePath,
    )
}

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
