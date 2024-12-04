package com.example.adminqe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminqe.adapter.OrderDetailsAdapter
import com.example.adminqe.adapter.PendingOrderAdapter
import com.example.adminqe.databinding.ActivityOrderDetailBinding
import com.example.adminqe.model.OrderDetails

class OrderDetail : AppCompatActivity() {
    private var _binding :ActivityOrderDetailBinding? = null
    private val binding get() = _binding!!

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodName: ArrayList<String> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()
    private var foodPrice: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBackOD.setOnClickListener {
            finish()
        }

        getOrdersDetails()
        binding.btnBackOD.setOnClickListener {
            finish()
        }
    }

    private fun getOrdersDetails() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails.let { orderDetails ->
            userName = receivedOrderDetails.userName
            foodName = receivedOrderDetails.foodNames as ArrayList<String>
            foodImages = receivedOrderDetails.foodImages as ArrayList<String>
            foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>
            foodPrice = receivedOrderDetails.foodPrice as ArrayList<String>
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            totalPrice = receivedOrderDetails.totalPrice

            setUserDetails()
            setAdapter()
        }

    }

    private fun setAdapter() {
        binding.recyclerOrderDetails.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this,foodName,foodImages,foodQuantity,foodPrice)
        binding.recyclerOrderDetails.adapter = adapter
    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalAmount.text = totalPrice
    }
}