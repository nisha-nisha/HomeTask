package com.example.hometask.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_color")
data class ProductColor(
   // @PrimaryKey `
    var product_id: String,
    var color: String
)