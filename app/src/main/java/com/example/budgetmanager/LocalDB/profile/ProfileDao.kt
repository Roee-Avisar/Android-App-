package com.example.budgetmanager.LocalDB.profile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.budgetmanager.Tables.Profile
import androidx.room.Query


@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserProfile(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUserProfile(profile: Profile)

    @Query("DELETE FROM user_profile_table")
    fun deleteUserProfile()

    @Query("SELECT * FROM user_profile_table LIMIT 1")
    fun getUserProfileLive(): LiveData<Profile?>

    @Query("SELECT * FROM user_profile_table LIMIT 1")
    fun getUserProfile(): Profile?


}