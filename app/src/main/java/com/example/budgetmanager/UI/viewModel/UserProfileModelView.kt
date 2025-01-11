package com.example.budgetmanager.UI.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetmanager.Tables.Item
import com.example.budgetmanager.Tables.Profile
import com.example.budgetmanager.repository.ItemsRepository
import com.example.budgetmanager.repository.ProfileRepository

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val profileRepository = ProfileRepository(application)
    private val itemRepository = ItemsRepository(application)

    private val _budgetLiveData = MutableLiveData<Double>()
    val budgetLiveData: LiveData<Double> get() = _budgetLiveData

    private val _expensesLiveData = MutableLiveData<Double>()
    val expensesLiveData: LiveData<Double> get() = _expensesLiveData

    private val _incomeLiveData = MutableLiveData<Double>()
    val incomeLiveData: LiveData<Double> get() = _incomeLiveData

    private val _userProfileLiveData = MutableLiveData<Profile?>()
    val userProfileLiveData: LiveData<Profile?> get() = _userProfileLiveData


    init {
        // Initialize LiveData with the current profile
        val currentProfile = profileRepository.getUserProfile()
        _userProfileLiveData.value = currentProfile
        _budgetLiveData.value = currentProfile?.initialBudget ?: 0.0
        _expensesLiveData.value = 0.0
        _incomeLiveData.value = 0.0
    }

    fun revertBudget(item: Item) {
        val amountChange = if (item.isExpense) item.amount else -item.amount
        val updatedBudget = (_budgetLiveData.value ?: 0.0) + amountChange

        _budgetLiveData.value = updatedBudget

        _userProfileLiveData.value?.let { currentProfile ->
            currentProfile.initialBudget = updatedBudget
            profileRepository.insertUserProfile(currentProfile)
        }
    }

    fun deleteUserProfile() {
        // Delete the current profile and reset all live data
        profileRepository.deleteUserProfile()
        itemRepository.deleteAll()

        // Reset all state variables to zero
        _userProfileLiveData.value = null
        _budgetLiveData.value = 0.0
        _expensesLiveData.value = 0.0
        _incomeLiveData.value = 0.0
    }

    fun insertUserProfile(profile: Profile) {
        // Insert the new profile and reset live data accordingly
        profileRepository.insertUserProfile(profile)
        _userProfileLiveData.value = profile

        // Initialize live data based on the new profile's initial budget
        _budgetLiveData.value = profile.initialBudget
        _expensesLiveData.value = 0.0
        _incomeLiveData.value = 0.0
    }


    fun updateBudget(amount: Double, isExpense: Boolean) {
        if (amount <= 0) {
            // Log or handle invalid amount
            return
        }

        _userProfileLiveData.value?.let { currentProfile ->
            val newBudget = if (isExpense) {
                _expensesLiveData.value = (_expensesLiveData.value ?: 0.0) + amount
                currentProfile.initialBudget - amount
            } else {
                _incomeLiveData.value = (_incomeLiveData.value ?: 0.0) + amount
                currentProfile.initialBudget + amount
            }

            currentProfile.initialBudget = newBudget
            profileRepository.insertUserProfile(currentProfile)
            _budgetLiveData.value = newBudget
        }
    }


}
