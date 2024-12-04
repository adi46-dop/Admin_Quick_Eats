package com.example.adminqe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.adminqe.databinding.ActivityAdminProfileBinding
import com.example.adminqe.model.AdminModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfile : AppCompatActivity() {
    private var _binding: ActivityAdminProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminDatabaseRef = database.reference.child("Admin")

        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.nameProfile.isEnabled = false
        binding.addressProfile.isEnabled = false
        binding.emailProfile.isEnabled = false
        binding.phoneProfile.isEnabled = false


        binding.btnEditProfile.setOnClickListener {
            binding.nameProfile.isEnabled = true
            binding.addressProfile.isEnabled = true
            binding.emailProfile.isEnabled = true
            binding.phoneProfile.isEnabled = true
            binding.btnSaveUserInfo.visibility = View.VISIBLE
            binding.nameProfile.requestFocus()

        }
        retrieveUserData()

        binding.btnSaveUserInfo.setOnClickListener {
            val updateName = binding.nameProfile.text.toString()
            val updateEmail = binding.emailProfile.text.toString()
            val updateAddress = binding.addressProfile.text.toString()
            val updatePhone = binding.phoneProfile.text.toString()

            val currentId = auth.currentUser?.uid
            if (currentId != null) {
                val userRef = adminDatabaseRef.child(currentId)
                userRef.child("nameOfOwner").setValue(updateName)
                userRef.child("email").setValue(updateEmail)
                userRef.child("address").setValue(updateAddress)
                userRef.child("phoneNumber").setValue(updatePhone)

                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Profile Updated Failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun retrieveUserData() {
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            val userRef = adminDatabaseRef.child(currentUserId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val ownerName = snapshot.child("nameOfOwner").value
                        val email = snapshot.child("email").value
                        val address = snapshot.child("address").value
                        val phoneNumber = snapshot.child("phoneNumber").value
                        Log.d("TAG", "onDataChange: $ownerName ,")
                        Log.d("OwnerName", "$ownerName")


                        setData(ownerName, email, address, phoneNumber)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AdminProfile, "Failed to show load data", Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    private fun setData(ownerName: Any?, email: Any?, address: Any?, phoneNumber: Any?) {
        binding.apply {
            nameProfile.setText(ownerName.toString())
            emailProfile.setText(email.toString())
            addressProfile.setText(address.toString())
            phoneProfile.setText(phoneNumber.toString())
        }
    }
}