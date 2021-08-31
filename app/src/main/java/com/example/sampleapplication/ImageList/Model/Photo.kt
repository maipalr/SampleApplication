package com.example.sampleapplication.ImageList.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "photos"
)
data class Photo(
    @PrimaryKey val id: String,
    val farm: String,
    val server: String,
    val secret: String,
    val title: String
): Serializable