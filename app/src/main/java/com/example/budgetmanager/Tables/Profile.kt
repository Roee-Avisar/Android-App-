package com.example.budgetmanager.Tables
import androidx.room.PrimaryKey

import androidx.room.Entity

@Entity(tableName = "user_profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 1,
    val firstName : String,
    val lastName : String,
    var initialBudget : Double,
    val imageUri : String? = null
)