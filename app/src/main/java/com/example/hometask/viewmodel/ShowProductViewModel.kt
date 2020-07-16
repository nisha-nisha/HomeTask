package com.example.hometask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.hometask.database.Product

class ShowProductViewModel(application: Application): AndroidViewModel(application) {

    val emptyProduct = MutableLiveData<Boolean>()
    val fullScreenStatus = MutableLiveData<Boolean>()

    init {
        emptyProduct.value = true
        fullScreenStatus.value = false
    }

}