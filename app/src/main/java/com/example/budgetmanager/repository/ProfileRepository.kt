package com.example.budgetmanager.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.budgetmanager.LocalDB.profile.ProfileDao
import com.example.budgetmanager.LocalDB.profile.ProfileDataBase
import com.example.budgetmanager.Tables.Profile

class ProfileRepository(application: Application) {

    private var profileDao: ProfileDao?

    init {
        val db = ProfileDataBase.getDataBase(application.applicationContext)
        profileDao = db?.profileDao()
    }
    fun insertUserProfile(profile: Profile) {
        profileDao?.insertUserProfile(profile)
    }

    fun getUserProfile(): Profile? {
        return profileDao?.getUserProfile()
    }

    fun deleteUserProfile() {
        profileDao?.deleteUserProfile()
    }

    fun getUserProfileLive(): LiveData<Profile?> {
        return profileDao?.getUserProfileLive() ?: MutableLiveData(null)
    }

}