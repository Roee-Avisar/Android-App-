package com.example.budgetmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.Tables.Profile
import com.example.budgetmanager.repository.ItemsRepository
import com.example.budgetmanager.repository.ProfileRepository


class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ItemsRepository(application)
    val items: LiveData<List<Item>> = repository.getItems()
    private val profileRepo = ProfileRepository(application)

    private val _chosenItem = MutableLiveData<Item>()
    val chosenItem : LiveData<Item> get() = _chosenItem

    fun setItem(item: Item){
        _chosenItem.value = item
    }


    fun addItem(item: Item, isExpense: Boolean) {
        repository.addItem(item, isExpense)
    }

    fun deleteAll(){
        repository.deleteAll()
    }
    fun deleteItem(item: Item){
        repository.deleteItem(item)
    }




    fun getUserProfile(): Profile? {
        return repository.getUserProfile()
    }

    fun updateBudget(amount: Double, isExpense: Boolean){
        val currentProfile = repository.getUserProfile()
        if (currentProfile != null){
            if(isExpense) {
                currentProfile.initialBudget -= amount
            }
            else{
                currentProfile.initialBudget += amount
            }
            profileRepo.insertUserProfile(currentProfile)
        }
    }
}