package com.example.budgetmanager.LocalDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgetmanager.LocalDB.item.ItemDao
import com.example.budgetmanager.LocalDB.profile.ProfileDao
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.Tables.Profile

@Database(entities = [Profile::class, Item::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun itemDao(): ItemDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getDataBase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db" // New database name
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // You may want to consider handling this more carefully in a production app
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}