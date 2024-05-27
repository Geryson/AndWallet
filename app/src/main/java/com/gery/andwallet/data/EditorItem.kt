package com.gery.andwallet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EditorItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val amount: Int,
)