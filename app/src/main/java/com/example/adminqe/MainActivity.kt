package com.example.adminqe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.adminqe.databinding.ActivityMainBinding
import com.example.adminqe.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.addMenu.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
        binding.AllItemMenu.setOnClickListener {
            startActivity(Intent(this, AlItemActivity::class.java))
        }
        binding.outForDelivery.setOnClickListener {
            startActivity(Intent(this, OutForDelivery::class.java))
        }
        binding.constraintLayout.setOnClickListener {
            startActivity(Intent(this, PendingOrders::class.java))
        }
        binding.profile.setOnClickListener {
            startActivity(Intent(this, AdminProfile::class.java))
        }

        binding.logout.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are sure want to logout ?")
                .setTitle("LOGOUT !!!")
                .setCancelable(false)
                .setPositiveButton("Yes"){ _,_ ->
                    auth.signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                .setNegativeButton("No"){dialog,_ ->
                    dialog.cancel()

                }
            val alertDialog = builder.create()
            alertDialog.show()

        }

        getPendingOrders()
        getCompletedOrders()
        getTotalEarnings()
    }

    private fun getTotalEarnings() {
        val listTotalEarning = mutableListOf<Int>()
        completedOrderRef = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)

                    completeOrder?.totalPrice?.replace("₹", "")?.toIntOrNull()
                        ?.let {
                            listTotalEarning.add(it)
                        }
                }
                binding.earnings.text = "₹"+ listTotalEarning.sum()
                    .toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to load total earnings", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun getCompletedOrders() {
        val completedOrdersRef = database.reference.child("CompletedOrder")
        var completedOrderItemCount = 0
        completedOrdersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                completedOrderItemCount = snapshot.childrenCount.toInt()
                binding.completdOrders.text = completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to load Complete Order details", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getPendingOrders() {
        val pendingOrdersRef = database.reference.child("OrderDetails")
        var pendingOrderItemCount = 0
        pendingOrdersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to load pending Orders", Toast.LENGTH_SHORT).show()
            }

        })
    }
}