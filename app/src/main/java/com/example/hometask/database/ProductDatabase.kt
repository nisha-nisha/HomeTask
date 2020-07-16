package com.example.hometask.database

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract val productDatabaseDao: ProductDatabaseDao
    companion object {
        var INSTANCE: ProductDatabase? = null

        fun getAppDataBase(context: Context): ProductDatabase? {
            if (INSTANCE == null){
                synchronized(ProductDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java, "productDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}