package com.example.budgetmanager.UI.viewModel

import androidx.lifecycle.ViewModel

class AddItemViewModel : ViewModel() {
    var amount: String? = null
    var description: String? = null
    var date: String? = null
    var imageUri: String? = null

    fun resetFields() {
        amount = null
        description = null
        date = null
        imageUri = null
    }
}