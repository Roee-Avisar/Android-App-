package com.example.budgetmanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetmanager.Tables.Profile
import com.example.budgetmanager.repository.ItemsRepository
import com.example.budgetmanager.repository.ProfileRepository


class UserProfileModelView(application: Application) : AndroidViewModel(application) {

    private val ProfileRepository = ProfileRepository(application)
    private val ItemRepository = ItemsRepository(application)

    private val _userProfileLiveData = MutableLiveData<Profile?>()
    val userProfileLiveData: LiveData<Profile?> get() = _userProfileLiveData

    init {
        // אתחול ה-LiveData בפרופיל הנוכחי
        _userProfileLiveData.value = ProfileRepository.getUserProfile()
    }

    fun insertUserProfile(profile: Profile) {
        ProfileRepository.insertUserProfile(profile)
        _userProfileLiveData.value = profile
    }

    fun deleteUserProfile() {
        ProfileRepository.deleteUserProfile()
        ItemRepository.deleteAll()
        _userProfileLiveData.value = null
    }


    fun getUserProfile(): Profile? {
        return ProfileRepository.getUserProfile()
    }

    fun updateBudget(amount: Double, isExpense: Boolean) {
        val currentProfile = ProfileRepository.getUserProfile()
        if (currentProfile != null) {
            if (isExpense) {
                currentProfile.initialBudget -= amount
            } else {
                currentProfile.initialBudget += amount
            }
            ProfileRepository.insertUserProfile(currentProfile)
        }
    }

    fun getInitialBudget(): Double {
        val currentProfile = ProfileRepository.getUserProfile()
        return currentProfile?.initialBudget ?: 0.0
    }

}