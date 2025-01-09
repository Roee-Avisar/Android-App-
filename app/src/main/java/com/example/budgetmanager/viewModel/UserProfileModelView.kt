package com.example.budgetmanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetmanager.Tables.Profile
import com.example.budgetmanager.repository.ItemsRepository
import com.example.budgetmanager.repository.ProfileRepository


class UserProfileModelView(application: Application) : AndroidViewModel(application) {

    private val profileRepository = ProfileRepository(application)
    private val ItemRepository = ItemsRepository(application)

    private val _budgetLiveData = MutableLiveData<Double>()
    val budgetLiveData: LiveData<Double> get() = _budgetLiveData


    private val _userProfileLiveData = MutableLiveData<Profile?>()
    val userProfileLiveData: LiveData<Profile?> get() = _userProfileLiveData

    init {
        // אתחול ה-LiveData בפרופיל הנוכחי
        val currentProfile = profileRepository.getUserProfile()
        _userProfileLiveData.value = currentProfile
        _budgetLiveData.value = currentProfile?.initialBudget ?: 0.0
    }

    fun insertUserProfile(profile: Profile) {
        profileRepository.insertUserProfile(profile)
        _userProfileLiveData.value = profile
    }

    fun deleteUserProfile() {
        profileRepository.deleteUserProfile()
        ItemRepository.deleteAll()
        _userProfileLiveData.value = null
    }


    fun getUserProfile(): Profile? {
        return profileRepository.getUserProfile()
    }

    fun updateBudget(amount: Double, isExpense: Boolean) {
        val currentProfile = profileRepository.getUserProfile()
        if (currentProfile != null) {
            if (isExpense) {
                currentProfile.initialBudget -= amount
            } else {
                currentProfile.initialBudget += amount
            }
            profileRepository.insertUserProfile(currentProfile)
            _budgetLiveData.value = currentProfile.initialBudget
        }
    }

    fun getInitialBudget(): Double {
        val currentProfile = profileRepository.getUserProfile()
        return currentProfile?.initialBudget ?: 0.0
    }

}