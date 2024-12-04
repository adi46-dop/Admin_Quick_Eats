package com.example.adminqe.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminqe.databinding.ActivityAlItemBinding
import com.example.adminqe.databinding.AllItemsBinding
import com.example.adminqe.model.AllMenu
import com.google.firebase.database.DatabaseReference

class AllItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    private val databaseReference: DatabaseReference,
    private val onDeleteClickedListener:(position: Int)-> Unit
) : RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>() {

    private val itemQuantities = IntArray(menuList.size) { 1 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val binding = AllItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AllItemViewHolder(private val binding: AllItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val qunatities = itemQuantities[position]

                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)

                txtCartFoodName.text = menuItem.foodName
                txtCartFoodPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(imgCart)


                btnDelete.setOnClickListener {
                    onDeleteClickedListener(position)
                }

            }
        }



    }


}