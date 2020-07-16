package com.example.hometask.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hometask.R
import com.example.hometask.database.Product
import com.example.hometask.database.ProductDatabase
import com.example.hometask.databinding.ActivityCreateProductBinding
import com.example.hometask.viewmodel.CreateProductViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_product.*
import java.util.*


class CreateProductActivity: AppCompatActivity() {

    private lateinit var createProductViewModel: CreateProductViewModel
    private lateinit var binding: ActivityCreateProductBinding
    private lateinit var productDatabase: ProductDatabase
    private val store: Dictionary<String, String> = Hashtable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createProductViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(CreateProductViewModel:: class.java)
        binding = DataBindingUtil.setContentView<ActivityCreateProductBinding>(this, R.layout.activity_create_product).apply {
            this.lifecycleOwner = this@CreateProductActivity
            this.viewModel = createProductViewModel
        }


        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayShowTitleEnabled(false)
        }

        init()
    }

    fun init() {
        productDatabase = ProductDatabase.getAppDataBase(this)!!
        getIntentData()
        initObservables()
        addStores()
    }

    private fun addStores() {
        store.put("101","Store 1")
        store.put("102","Store 2")
        store.put("103","Store 3")
        store.put("104","Store 4")

        createProductViewModel.stores.value = store
    }



    private fun getIntentData(){
        val extras = intent.extras ?: return
        val productDetail = extras.getString("PRODUCT_DETAIL")
        val product = Gson().fromJson(productDetail, Product::class.java)

        createProductViewModel.id.value = product.id
        createProductViewModel.etName.value = product.name
        createProductViewModel.etDescription.value = product.description
        createProductViewModel.etRegularPrice.value = product.regular_price.toString()
        createProductViewModel.etSalePrice.value = product.sale_price.toString()
        createProductViewModel.etColor.value = product.color
        createProductViewModel.storeName.value = product.store
        createProductViewModel.imageName.value = product.product_photo.substring(
            product.product_photo.lastIndexOf('/') + 1)

    }

    private fun initObservables() {
        createProductViewModel.error.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

        createProductViewModel.submit.observe(this, Observer {

            if (createProductViewModel.id.value.isNullOrEmpty()) {
                println("***** insert")
                InsertProduct(this, it).execute()
            }
            else {
                UpdateProduct(this, it).execute()
            }

        })

        createProductViewModel.uploadImage.observe(this, Observer {
            if(it!!) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                        //permission denied
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else{
                    //system OS is < Marshmallow
                    pickImageFromGallery();
                }
            }
        })
    }

    private class InsertProduct(var context: CreateProductActivity, var product: Product) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.productDatabase.productDatabaseDao.insert(product)
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Toast.makeText(context, "Added to Database", Toast.LENGTH_LONG).show()
            }
        }
    }

    private class UpdateProduct(var context: CreateProductActivity, var product: Product) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.productDatabase.productDatabaseDao.update(product)
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show()
                context.finish()
            }
        }
    }

    private fun pickImageFromGallery() {
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;
        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
            ?: return contentUri.path
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null){

            val image = getRealPathFromURI(data.data!!)
            println("***** "+image)
            createProductViewModel.imagePath.value = image.toString()
            createProductViewModel.imageName.value = createProductViewModel.imagePath.value!!.toString().substring(
                createProductViewModel.imagePath.value!!.toString().lastIndexOf('/') + 1)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}