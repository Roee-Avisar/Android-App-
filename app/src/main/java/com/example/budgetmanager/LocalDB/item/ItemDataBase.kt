package com.example.budgetmanager.LocalDB.item

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgetmanager.LocalDB.profile.ProfileDao
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.Tables.Profile

@Database(entities = [Item::class, Profile::class], version = 2)
abstract class ItemDataBase : RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun profileDao(): ProfileDao

    companion object {

        @Volatile
        private var instance: ItemDataBase? = null

        fun getDataBase(context: Context): ItemDataBase {
            return instance ?: synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDataBase::class.java,
                    "item_db"
                )
                    .fallbackToDestructiveMigration()  // Consider defining migrations for production
                    .build()

                instance = tempInstance
                tempInstance
            }
        }
    }
}