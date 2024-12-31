package com.example.budgetmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.budgetmanager.Tables.Profile
import com.example.budgetmanager.repository.ProfileRepository


class UserProfileModelView(application: Application) : AndroidViewModel(application) {

    private val repository = ProfileRepository(application)
    val userProfileLiveData: LiveData<Profile?> = repository.getUserProfileLive()!!


    val profileLiveData: LiveData<Profile?>? = repository.getUserProfileLive()

    fun saveUserProfile(profile: Profile) {
        repository.insertUserProfile(profile)
    }

    fun deleteUserProfile() {
        repository.deleteUserProfile()
    }

    fun getUserProfile(): Profile? {
        return repository.getUserProfile()
    }


}