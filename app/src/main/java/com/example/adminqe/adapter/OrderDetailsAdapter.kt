package com.example.adminqe.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminqe.databinding.OrderDetailsItemsBinding

class OrderDetailsAdapter(
    private var context: Context,
    private var foodName: ArrayList<String>,
    private var foodImage: ArrayList<String>,
    private var foodQuantity: ArrayList<Int>,
    private var foodPrice: ArrayList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
       val binding = OrderDetailsItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodName.size
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
       holder.bind(position)
    }

    inner class OrderDetailsViewHolder(private val binding: OrderDetailsItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.apply {
                txtCustomerNameOD.text = foodName[position]
                txtFoodPriceOD.text = foodPrice[position]
                txtQunatityOD.text = foodQuantity[position].toString()
                val uri = foodImage[position]
                val image = Uri.parse(uri)
                Glide.with(context).load(image).into(imgCustomerCart)
            }
        }
    }

}