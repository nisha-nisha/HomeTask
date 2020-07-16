package com.example.hometask.view

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hometask.R
import com.example.hometask.adapter.ProductListAdapter
import com.example.hometask.database.Product
import com.example.hometask.database.ProductDatabase
import com.example.hometask.databinding.ActivityShowProductBinding
import com.example.hometask.viewmodel.ShowProductViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_product.*
import kotlinx.android.synthetic.main.activity_create_product.toolbar
import kotlinx.android.synthetic.main.activity_show_product.*
import kotlinx.android.synthetic.main.product_full_image.*
import java.io.File

class ShowProductActivity: AppCompatActivity(), ProductListAdapter.OnEditClickListener, ProductListAdapter.OnDeleteClickListener, ProductListAdapter.OnImageClickListener {

    private lateinit var showProductViewModel: ShowProductViewModel
    private lateinit var binding: ActivityShowProductBinding
    private lateinit var productDatabase: ProductDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProductViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(ShowProductViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityShowProductBinding>(this,R.layout.activity_show_product).apply {
            this.lifecycleOwner = this@ShowProductActivity
            this.viewModel = showProductViewModel
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayShowTitleEnabled(false)
        }

        init()

    }

    private fun init() {
        productDatabase = ProductDatabase.getAppDataBase(this)!!
        GetAllProduct(this).execute()
        initListener()
    }

    private fun initListener() {
        ib_close.setOnClickListener {
            showProductViewModel.fullScreenStatus.value = false
        }
    }

    private class GetAllProduct(var context: ShowProductActivity): AsyncTask<Void, Void, List<Product>?>(){

        override fun doInBackground(vararg params: Void?): List<Product>? {
           val list = context.productDatabase.productDatabaseDao.getAllProducts()
            return list
        }

        override fun onPostExecute(list: List<Product>?) {
            if (list != null && list.size>0){
                println("**********"+list.size)
                context.showProductViewModel.emptyProduct.value = false
                context.setProductAdapter(list)
            }
            else
            {
                context.showProductViewModel.emptyProduct.value = true
            }
        }

    }


    private class DeleteProduct(var context: ShowProductActivity, var product: Product ): AsyncTask<Void, Void, Boolean>(){

        override fun doInBackground(vararg params: Void?): Boolean {
            context.productDatabase.productDatabaseDao.clearProduct(product.id)
            return true
        }

        override fun onPostExecute(bool: Boolean?) {
            if (bool!!){
                Toast.makeText(context, context.getString(R.string.deleted), Toast.LENGTH_SHORT).show()
                GetAllProduct(context).execute()
            }
        }

    }


    private fun setProductAdapter(list: List<Product>){

        val productListAdapter: ProductListAdapter = ProductListAdapter(this,list,this,this, this)
        binding.recyclerview.apply {

            layoutManager = LinearLayoutManager(this@ShowProductActivity, LinearLayoutManager.VERTICAL, false)
            adapter = productListAdapter
        }
    }

    private fun openCreateProductActivity(product: Product){
        startActivity(Intent(this,CreateProductActivity::class.java)
            .putExtra("PRODUCT_DETAIL", Gson().toJson(product))
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onEditClick(product: Product, view: View?) {
        openCreateProductActivity(product)
    }

    override fun onDeleteClick(product: Product, view: View?) {
        DeleteProduct(this, product).execute()
    }

    override fun onRestart() {
        super.onRestart()
        GetAllProduct(this).execute()
    }

    override fun onImageClick(imagePath: String) {
        val photoUri = Uri.fromFile( File(imagePath))
        Glide.with(this).load(photoUri).into(iv_full_image)
        showProductViewModel.fullScreenStatus.value = true

    }


}