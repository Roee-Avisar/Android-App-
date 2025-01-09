package com.example.budgetmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.repository.ItemRepository

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val itemRepository = ItemRepository(application)

    // LiveData to track current budget
    private val currentBudget = MutableLiveData<Double>().apply { value = 0.0 }

    private val _chosenItem = MutableLiveData<Item?>()
    val chosenItem: LiveData<Item?> get() = _chosenItem

    // Getter to expose currentBudget as LiveData (immutable)
    val currentBudgetLiveData: LiveData<Double>
        get() = currentBudget

    // Get all items
    fun getItems() = itemRepository.getItems()

    // Add an item and update budget
    suspend fun addItem(item: Item) {
        itemRepository.addItem(item)
        updateBudget(item.amount, item.isExpense)
    }

    // Delete an item and update budget
    suspend fun deleteItem(item: Item) {
        itemRepository.deleteItem(item)
        updateBudget(item.amount, item.isExpense, isDeleting = true)
    }

    // Delete all items and reset budget to 0
    suspend fun deleteAllItems() {
        itemRepository.deleteAllItems()
        currentBudget.value = 0.0  // Reset current budget to 0
    }

    // Private method to update budget based on item addition or deletion
    private fun updateBudget(amount: Double, isExpense: Boolean, isDeleting: Boolean = false) {
        val current = currentBudget.value ?: 0.0
        currentBudget.value = if (isExpense) {
            if (isDeleting) {
                current + amount // Revert if deleting an expense
            } else {
                current - amount // Subtract for expense
            }
        } else {
            if (isDeleting) {
                current - amount // Revert if deleting an income
            } else {
                current + amount // Add for income
            }
        }
    }

    // Expose current budget as LiveData
    fun getCurrentBudget(): LiveData<Double> {
        return currentBudgetLiveData
    }
}
