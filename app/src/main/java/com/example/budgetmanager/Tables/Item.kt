package com.example.budgetmanager.Tables


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "item")
data class Item(

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    @ColumnInfo(name = "price")
    val amount: Double,

    @ColumnInfo(name = "content_desc")
    val description: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "image")
    val photo: String? = null,

    @ColumnInfo(name = "is_expense")
    val  isExpense : Boolean)
    : Parcelable {


}