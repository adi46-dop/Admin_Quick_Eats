package com.example.adminqe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminqe.adapter.AllItemAdapter
import com.example.adminqe.databinding.ActivityAlItemBinding
import com.example.adminqe.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AlItemActivity : AppCompatActivity() {
    private var _binding : ActivityAlItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var allItemAdapter : AllItemAdapter
    private lateinit var databaseRef : DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems : ArrayList<AllMenu> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAlItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseRef = FirebaseDatabase.getInstance().reference
        retrieveMenuItem()

        binding.imageButton.setOnClickListener {
            finish()
        }
        

    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clearing existing data before populating
                menuItems.clear()

                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error:${error.message}")
            }

        })
    }

    private fun setAdapter() {

        allItemAdapter = AllItemAdapter(this,menuItems,databaseRef){ position ->
            deleteMenuItems(position)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = allItemAdapter
    }

    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete = menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuRef = database.reference.child("menu").child(menuItemKey!!)
        foodMenuRef.removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){
                menuItems.removeAt(position)
                binding.recyclerView.adapter?.notifyItemRemoved(position)
            }else {
                Toast.makeText(this, "Failed to delete Item", Toast.LENGTH_SHORT).show()
            }
        }
    }
}