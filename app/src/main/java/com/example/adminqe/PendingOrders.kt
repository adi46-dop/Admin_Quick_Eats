package com.example.adminqe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminqe.adapter.PendingOrderAdapter
import com.example.adminqe.databinding.ActivityPendingOrdersBinding
import com.example.adminqe.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrders : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private var _binding: ActivityPendingOrdersBinding? = null
    private val binding get() = _binding!!
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice:MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPendingOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")
        binding.imgBackPendingOrders.setOnClickListener {
            finish()
        }
        getOrdersDetails()

    }

    private fun getOrdersDetails() {
        //retrieving order details form database

        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderDetailsSnapshot in snapshot.children){
                    val orderDetails = orderDetailsSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }

                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for( orderItem in listOfOrderItem){
            //add data to respective list for populating recycler View

            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.rVPendingOrders.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this,listOfName,listOfImageFirstFoodOrder,listOfTotalPrice,this)
        binding.rVPendingOrders.adapter = adapter
    }

    override fun onItemClickedListener(position: Int) {
        val intent  = Intent(this, OrderDetail::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickedListener(position: Int) {
        // handling item acceptance and update database
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderRefrence = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderRefrence?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }

    private fun updateOrderAcceptStatus(position: Int) {
        //update order acceptance in user buy History and order-details
         val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistory = database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistory.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }

    override fun onItemDispatchClickedListener(position: Int) {
        //handle item dispatch and update database
        val dispatchedItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchedItemOrderRef = database.reference.child("CompletedOrder").child(dispatchedItemPushKey!!)
        dispatchedItemOrderRef.setValue(listOfOrderItem[position]).addOnSuccessListener {
            deleteitemOrderDetails(dispatchedItemPushKey)
        }
    }

    private fun deleteitemOrderDetails(dispatchedItemPushKey: String) {
        val orderDetails = database.reference.child("OrderDetails").child(dispatchedItemPushKey)
        orderDetails.removeValue().addOnSuccessListener {
            Toast.makeText(this,"Order is Dispatched",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Failed to Dispatched",Toast.LENGTH_SHORT).show()
        }
    }

}