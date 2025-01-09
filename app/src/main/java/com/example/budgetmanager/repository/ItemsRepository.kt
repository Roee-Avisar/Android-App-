package com.example.budgetmanager.repository

import android.app.Application
import com.example.budgetmanager.LocalDB.item.ItemDao
import com.example.budgetmanager.LocalDB.item.ItemDataBase
import com.example.budgetmanager.Tables.Item

class ItemRepository(application: Application) {

    private val itemDao: ItemDao

    init {
        val db = ItemDataBase.getDataBase(application.applicationContext)
        itemDao = db.itemDao()
        // You can initialize currentBudget from the database or default to 0.0
    }

    // Get all items
    fun getItems() = itemDao.getItems()

    // Add an item and update budget
    suspend fun addItem(item: Item) {
        itemDao.addItem(item)
    }

    // Delete an item and update budget
    suspend fun deleteItem(item: Item) {
        itemDao.deleteItem(item)
    }

    // Get item by id
    fun getItem(id: Int) = itemDao.getItem(id)

    // Delete all items
    suspend fun deleteAllItems() {
        itemDao.deleteAll()
    }



    // Get the current budget
    fun getCurrentBudget(items: List<Item>): Double {
        // Use sumOf instead of sumByDouble
        return items.sumOf { if (it.isExpense) -it.amount else it.amount }
    }
}
