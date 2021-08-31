package com.example.sampleapplication.ImageList.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.sampleapplication.ImageList.Model.Photo

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(photo: Photo): Long

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Delete
    suspend fun deletePhoto(photo: Photo)
}