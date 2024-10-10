package com.macpry.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<KMPDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(KMP_DATABASE_NAME)
    return Room.databaseBuilder<KMPDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
