package com.example.budgetmanager.LocalDB.item


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgetmanager.Tables.Item

@Dao
interface ItemDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)

    @Delete
    fun deleteItem(vararg: Item)

    @Update
    fun update(item: Item)

    @Query("SELECT * FROM item")
    fun getItems() : LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE id LIKE :id")
    fun getItem(id:Int) : Item?

    @Query("DELETE  FROM item")
    fun deleteAll()
}
