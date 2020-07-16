package com.example.hometask.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hometask.database.Product
import com.example.hometask.databinding.ProductListRowBinding
import java.io.File
import java.net.URI

class ProductListAdapter(val context: Context,val list: List<Product>, val editListener : OnEditClickListener, val deleteListener : OnDeleteClickListener, val imageListener : OnImageClickListener): RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductListRowBinding.inflate(inflater)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.viewModel = list[position]

        holder.binding.btnEdit.setOnClickListener {
            editListener.onEditClick(list[position],it)
        }

        holder.binding.btnDelete.setOnClickListener {
            deleteListener.onDeleteClick(list[position],it)
        }

        holder.binding.ivImage.setOnClickListener {
            imageListener.onImageClick(list[position].product_photo)
        }

        val photoUri = Uri.fromFile( File( list[position].product_photo))
        Glide.with(context).load(photoUri).into(holder.binding.ivImage)

        try {
            holder.binding.llColor.setBackgroundColor(Color.parseColor(list[position].color))
        } catch (e: Exception) {
        }
    }

    inner class ViewHolder(binding: ProductListRowBinding): RecyclerView.ViewHolder(binding.root){
        val binding: ProductListRowBinding = binding
    }

    interface OnEditClickListener {
        fun onEditClick(product: Product, view: View?)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(product: Product, view: View?)
    }

    interface OnImageClickListener {
        fun onImageClick(imagePath: String)
    }

}