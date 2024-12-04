package com.example.adminqe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminqe.adapter.DeliveryAdapter
import com.example.adminqe.databinding.ActivityOutForDeliveryBinding
import com.example.adminqe.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDelivery : AppCompatActivity() {
    private var _binding : ActivityOutForDeliveryBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOutForDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrieveCompleteOrderDetails()
        binding.imgBackOFD.setOnClickListener {
            finish()
        }

    }

    private fun retrieveCompleteOrderDetails() {
        database = FirebaseDatabase.getInstance()
        val completeOrderRef = database.reference.child("CompletedOrder").orderByChild("currentTime")
        completeOrderRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()
                for(orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OutForDelivery, "Failed to fetch data...", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setDataIntoRecyclerView() {
        // initialization list to hold customer name and payment status

        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for(order in listOfCompleteOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }

        val adapter = DeliveryAdapter(customerName,moneyStatus)
        binding.recyclerViewOFD.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOFD.adapter = adapter
    }


}