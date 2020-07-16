package com.example.hometask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val productShow = MutableLiveData<Boolean>()
    val productCreate = MutableLiveData<Boolean>()

    fun onProductShow() {
        productShow.value = true
    }

    fun onProductCreate() {
        productCreate.value = true
    }

}