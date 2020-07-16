package com.example.hometask.database

import androidx.room.Entity

@Entity(tableName = "product_color")
data class ProductColor(
    var product_id: String,
    var color: String
 )