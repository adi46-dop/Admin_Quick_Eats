package com.example.adminqe.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminqe.databinding.OutForDeliveryItemBinding

class DeliveryAdapter(private val customerNames : MutableList<String>,private val moneyStatus : MutableList<Boolean>): RecyclerView.Adapter<DeliveryAdapter.DelivaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DelivaryViewHolder {
        val binding = OutForDeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DelivaryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    override fun onBindViewHolder(holder: DelivaryViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DelivaryViewHolder(private val binding: OutForDeliveryItemBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.apply {
                txtCustomerName.text = customerNames[position]
                if(moneyStatus[position] == true){
                    txtMoneyStatus.text = "Received"
                }else {
                    txtMoneyStatus.text = "NotReceived"
                }

                val colorMap = mapOf(
                    true to Color.GREEN,
                    false to Color.RED,
                )

                txtMoneyStatus.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)
                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)
            }
        }
    }
}