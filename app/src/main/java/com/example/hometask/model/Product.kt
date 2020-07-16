package com.example.hometask.model

import java.util.*

data class Product(var id: Int, var name: String, var description: String, var regular_price: Double, var sale_price: Double, var product_photo: String, var colors: List<Color>, var stores: Dictionary<String, String>)
data class Color(var color_code: String)
