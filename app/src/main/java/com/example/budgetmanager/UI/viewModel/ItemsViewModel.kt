package com.example.budgetmanager.UI.viewModel

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

    fun deleteItem(item: Item){
        repository.deleteItem(item)
    }

    fun addItem(item: Item, isExpense: Boolean) {
        repository.addItem(item, isExpense)
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    fun getItemById(id: Int): Item? {
        return repository.getItem(id)
    }

    fun updateItem(updatedItem: Item, oldItem: Item) {
        repository.deleteItem(oldItem)
        profileRepo.updateBudget(oldItem.amount * -2, oldItem.isExpense)
        repository.addItem(updatedItem, updatedItem.isExpense)
        profileRepo.updateBudget(updatedItem.amount, updatedItem.isExpense)
    }



}