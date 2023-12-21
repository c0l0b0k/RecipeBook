package com.example.recipebook.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipeDt")
data class NameEntity(
    @PrimaryKey()
    val id: String ,
    val name: String,
    val instructions:String

)
