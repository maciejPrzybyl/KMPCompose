package com.macpry.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.dsl.module

actual val databaseModule
    get() = module {
        single<KMPDatabase> { getKMPDatabase(get()) }
    }

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<KMPRoomDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPRoomDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

fun getKMPDatabase(ctx: Context): KMPRoomDatabase =
    getRoomDatabase(getDatabaseBuilder(ctx))
