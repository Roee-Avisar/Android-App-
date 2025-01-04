package com.example.budgetmanager.LocalDB.profile

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgetmanager.Tables.Profile

@Database(entities = [Profile::class], version = 2, exportSchema = false)
abstract class ProfileDataBase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {

        @Volatile
        private var instance: ProfileDataBase? = null

        fun getDataBase(context: Context) = instance ?: kotlin.synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                ProfileDataBase::class.java,
                "profile_db"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }

}


