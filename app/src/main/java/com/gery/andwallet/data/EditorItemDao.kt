package com.gery.andwallet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EditorItemDao {
    @Insert
    fun insert(item: EditorItem)

    @Update
    fun update(item: EditorItem)

    @Delete
    fun delete(item: EditorItem)

    @Query("DELETE FROM EditorItem")
    fun deleteAll()

    @Query("SELECT * FROM EditorItem ORDER BY name ASC")
    fun getAllItems(): MutableList<EditorItem>
}