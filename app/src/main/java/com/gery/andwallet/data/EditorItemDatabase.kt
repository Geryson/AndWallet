package com.gery.andwallet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [EditorItem::class],
    version = 1,
    exportSchema = false
)
abstract class EditorItemDatabase : RoomDatabase() {
    abstract fun editorItemDao(): EditorItemDao

    companion object {
        private var INSTANCE: EditorItemDatabase? = null

        fun getInstance(context: Context): EditorItemDatabase {
            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    EditorItemDatabase::class.java, "andwallet.db").build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}