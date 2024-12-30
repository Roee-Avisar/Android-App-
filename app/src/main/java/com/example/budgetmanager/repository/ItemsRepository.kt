package com.example.budgetmanager.repository

import android.app.Application
import com.example.budgetmanager.LocalDB.item.ItemDao
import com.example.budgetmanager.LocalDB.item.ItemDataBase
import com.example.budgetmanager.Tables.Item

class ItemsRepository(application: Application) {

    private  var itemDao: ItemDao

    init {
        val db = ItemDataBase.getDataBase(application.applicationContext)
        itemDao = db.itemDao()
    }

    fun getItems() = itemDao.getItems()

    fun addItem(item: Item){
        itemDao.addItem(item)
    }

    fun deleteItem(item: Item){
        itemDao.deleteItem(item)
    }

    fun getItem(id : Int) = itemDao.getItem(id)

    fun deleteAll(){
        itemDao.deleteAll()
    }

}