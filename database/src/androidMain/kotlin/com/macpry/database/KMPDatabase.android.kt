package com.macpry.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val databaseModule
    get() = module {
        singleOf(::getKMPDatabase)
    }

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<KMPDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

fun getKMPDatabase(ctx: Context): KMPDatabase =
    getRoomDatabase(getDatabaseBuilder(ctx))
