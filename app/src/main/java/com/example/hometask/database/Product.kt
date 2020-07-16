package com.example.hometask.database

import android.net.Uri
import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey
    var id: String,
    var name: String,
    var description: String,
    var regular_price: Double,
    var sale_price: Double,
    var product_photo: String,
    var color: String,
    var store: String
    )
//data class Color(var color_code: String)
