package com.example.hometask.viewmodel

import android.app.Application
import android.net.Uri
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.hometask.R
import com.example.hometask.database.Product
import java.util.*

class CreateProductViewModel(application: Application): AndroidViewModel(application) {
    val id = MutableLiveData<String>()
    val etName = MutableLiveData<String>()
    val etDescription = MutableLiveData<String>()
    val etRegularPrice = MutableLiveData<String>()
    val etSalePrice = MutableLiveData<String>()
    val etColor = MutableLiveData<String>()
    val imagePath = MutableLiveData<String>()
    val imageName = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    val submit = MutableLiveData<Product>()
    val uploadImage = MutableLiveData<Boolean>()
    val stores = MutableLiveData<Dictionary<String, String>>()
    val etStores = MutableLiveData<String>()
    val storeName = MutableLiveData<String>()


    fun onSubmit() {

        if (addProductValidate()) {
            val product: Product
            if (id.value.isNullOrEmpty()) {
                product = Product(UUID.randomUUID().toString(), etName.value!!, etDescription.value!!, etRegularPrice.value!!.toDouble(), etSalePrice.value!!.toDouble(), imagePath.value!!, etColor.value!!, storeName.value!!  )
            }
            else {
                product = Product(id.value!!, etName.value!!, etDescription.value!!, etRegularPrice.value!!.toDouble(), etSalePrice.value!!.toDouble(), imagePath.value!!, etColor.value!!, storeName.value!!  )
            }

            submit.value = product
        }
    }

    fun onAddColor() {

    }

    fun onUploadImage() {
        uploadImage.value = true
    }

    private fun addProductValidate(): Boolean {
        var status = true
        if (etName.value.isNullOrEmpty() ) {
            error.value = this.getApplication<Application>().getString(R.string.enter_name)
            status = false
        }
        else if (etDescription.value == null ) {
            error.value = this.getApplication<Application>().getString(R.string.enter_description)
            status = false
        }
        else if (etRegularPrice.value == null ) {
            error.value = this.getApplication<Application>().getString(R.string.enter_regular_price)
            status = false
        }
        else if (etSalePrice.value == null ) {
            error.value = this.getApplication<Application>().getString(R.string.enter_sale_price)
            status = false
        }
        else if (etColor.value == null ) {
            error.value = this.getApplication<Application>().getString(R.string.enter_color_code)
            status = false
        }
        else if (!etColor.value!!.contains("#") || (etColor.value!!.length != 7) ) {
            error.value = this.getApplication<Application>().getString(R.string.enter_valid_color_code)
            status = false
        }
        else if (etStores.value == null ) {
            error.value = this.getApplication<Application>().getString(R.string.enter_store_key)
            status = false
        }
        else if (storeName.value == null ) {
            error.value = this.getApplication<Application>().getString(R.string.click_enter_button)
            status = false
        }
        else if (imagePath.value == null ) {
            error.value = this.getApplication<Application>().getString(R.string.upload_image)
            status = false
        }

        return status
    }

    fun onEnterStore() {
        if (!etStores.value.isNullOrEmpty()) {
            storeName.value = stores.value!!.get(etStores.value)
        }
        else{
            error.value = this.getApplication<Application>().getString(R.string.enter_store_key)
        }
    }

}