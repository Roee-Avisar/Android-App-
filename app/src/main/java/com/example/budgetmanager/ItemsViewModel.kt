package com.example.budgetmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.repository.ItemsRepository


class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ItemsRepository(application)
    val items: LiveData<List<Item>> = repository.getItems()

    private val _chosenItem = MutableLiveData<Item>()
    val chosenItem : LiveData<Item> get() = _chosenItem

    fun setItem(item: Item){
        _chosenItem.value = item
    }

    fun addItem(item: Item){
        repository.addItem(item)
    }

    fun deleteItem(item: Item){
        repository.deleteItem(item)
    }

    fun deleteAll(){
        repository.deleteAll()
    }
}