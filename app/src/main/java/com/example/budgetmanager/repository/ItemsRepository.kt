package com.example.budgetmanager.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.budgetmanager.LocalDB.AppDatabase
import com.example.budgetmanager.LocalDB.item.ItemDao
import com.example.budgetmanager.LocalDB.profile.ProfileDao
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.Tables.Profile

class ItemsRepository(application: Application) {

    private val profileDao: ProfileDao
    private var itemDao: ItemDao

    init {
        val db = AppDatabase.getDataBase(application.applicationContext)
        itemDao = db.itemDao()
        profileDao = db.profileDao()
    }

    fun getItems(): LiveData<List<Item>> = itemDao.getItems()

    fun addItem(item: Item, isExpense: Boolean) {
        itemDao.addItem(item)
    }

    fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }

    fun getItem(id: Int) = itemDao.getItem(id)

    fun deleteAll() {
        itemDao.deleteAll()
    }

    fun getUserProfile(): Profile? {
        return profileDao.getUserProfile()
    }

    fun updateItem(updatedItem: Item, isExpense: Boolean) {
        itemDao.update(updatedItem)
    }

}