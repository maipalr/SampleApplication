package com.example.sampleapplication.ImageList.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.sampleapplication.ImageList.Model.Photo
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [Photo::class],
    version = 1
)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun getPhotoDao(): PhotoDao

    companion object{
        @Volatile
        private  var instance: PhotoDatabase? = null

        operator fun invoke(context: Context) = instance ?: databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            "photo_db.db"
        ).build()
    }
}