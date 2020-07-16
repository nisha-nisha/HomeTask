package com.example.hometask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hometask.R
import com.example.hometask.databinding.ActivityMainBinding
import com.example.hometask.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(MainViewModel:: class.java)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main).apply {
            this.lifecycleOwner = this@MainActivity
            this.viewModel = mainViewModel
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            this.setDisplayShowTitleEnabled(false)
        }

        init()
    }

    fun init() {
        initObservables()
    }

    private fun initObservables() {
        mainViewModel.productCreate.observe(this, Observer {
            if (it!!) {
                openCreateProductActivity()
            }
        })

        mainViewModel.productShow.observe(this, Observer {
            if (it!!) {
                openShowProductActivity()
            }
        })
    }

    private fun openCreateProductActivity() {
        startActivity(Intent(this, CreateProductActivity::class.java))
    }

    private fun openShowProductActivity(){
        startActivity(Intent(this,ShowProductActivity::class.java))
    }
}
