package com.example.adminqe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.adminqe.databinding.ActivitySignBinding
import com.example.adminqe.model.AdminModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignActivity : AppCompatActivity() {
    private var _binding : ActivitySignBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth : FirebaseAuth
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var location : String
    private lateinit var nameOfOwner : String
    private lateinit var nameOfRestaurant : String
    private lateinit var databaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize firebase auth
        mAuth =  FirebaseAuth.getInstance()
        databaseRef  = FirebaseDatabase.getInstance().reference

        binding.btnCreateAccount.setOnClickListener {
            email = binding.edtEmailAddress.text.toString().trim()
            password = binding.edtTextPassword.text.toString().trim()
            nameOfOwner = binding.edtOwnerName.text.toString().trim()
            nameOfRestaurant = binding.edtRestaurantName.text.toString().trim()

            if(email.isBlank() || password.isBlank() || nameOfOwner.isBlank() || nameOfRestaurant.isBlank()){
                Toast.makeText(this,"Please Enter the required field",Toast.LENGTH_SHORT).show()
            } else{
                createAccount(email,password)
            }
        }
        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    //Creating Account
    private fun createAccount(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Account created successfully",Toast.LENGTH_SHORT).show()
                saveAccountDetails()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this,"Failed To create account",Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount:Failure",task.exception)
            }
        }.addOnFailureListener(this) { exception ->
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAccountDetails() {
        email = binding.edtEmailAddress.text.toString().trim()
        password = binding.edtTextPassword.text.toString().trim()
        nameOfOwner = binding.edtOwnerName.text.toString().trim()
        nameOfRestaurant = binding.edtRestaurantName.text.toString().trim()

        val admin = AdminModel(nameOfRestaurant,nameOfOwner,email,password)

        val adminId = FirebaseAuth.getInstance().currentUser!!.uid
        if (adminId != null) {
            databaseRef.child("Admin").child(adminId).setValue(admin)
        }
//        adminId?.let {
//            databaseRef.child("Admin").child(it).setValue(admin)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}