package com.example.budgetmanager.Tables
import androidx.room.PrimaryKey

import androidx.room.Entity

@Entity(tableName = "user_profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = false)
    val id : Int = 1,


    val firstName : String,
    val lastName : String,
    val initialBudget : Double,
    val imageUri : String? = null

)