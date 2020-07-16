package com.example.hometask.database

import androidx.room.*

@Dao
interface ProductDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Query("SELECT * from product_table WHERE id = :key")
    fun get(key: String): Product?

    @Query("DELETE FROM product_table WHERE id = :key")
    fun clearProduct(key: String)

    @Query("DELETE FROM product_table")
    fun clearAll()

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): List<Product>
}


