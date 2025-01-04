package com.example.budgetmanager.repository

import android.app.Application
import com.example.budgetmanager.LocalDB.item.ItemDao
import com.example.budgetmanager.LocalDB.item.ItemDataBase
import com.example.budgetmanager.LocalDB.profile.ProfileDao
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.Tables.Profile

class ItemsRepository(application: Application) {

    private val profileDao: ProfileDao
    private  var itemDao: ItemDao

    init {
        val db = ItemDataBase.getDataBase(application.applicationContext)
        itemDao = db.itemDao()
        profileDao = db.profileDao()
    }

    fun getItems() = itemDao.getItems()

    fun addItem(item: Item, isExpense: Boolean){
        itemDao.addItem(item)
        updateBudget(item.amount, isExpense)
    }

    fun deleteItem(item: Item){
        itemDao.deleteItem(item)
    }

    fun getItem(id : Int) = itemDao.getItem(id)

    fun deleteAll(){
        itemDao.deleteAll()
    }

    fun getUserProfile() : Profile?{
        return profileDao.getUserProfile()
    }

    fun updateBudget(amount: Double, isExpense: Boolean) {
        val currentProfile = profileDao.getUserProfile()
        if (currentProfile != null) {
            if (isExpense) {
                currentProfile.initialBudget -= amount
            } else {
                currentProfile.initialBudget += amount
            }
            profileDao.insertUserProfile(currentProfile)
        }
    }

}