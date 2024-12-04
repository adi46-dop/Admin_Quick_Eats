package com.example.adminqe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminqe.databinding.ActivityAddItemBinding
import com.example.adminqe.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddItemActivity : AppCompatActivity() {
    private var _binding: ActivityAddItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodItemName: String
    private lateinit var foodPrice: String
    private lateinit var foodDesc: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseRef: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance()


        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.btnAddItem.setOnClickListener {
            foodItemName = binding.edtItemName.text.toString().trim()
            foodPrice = binding.editItemPrice.text.toString().trim()
            foodDesc = binding.edtDesc.text.toString().trim()
            foodIngredient = binding.edtIngredients.text.toString().trim()

            if (foodItemName.isBlank() || foodPrice.isBlank() || foodDesc.isBlank() || foodIngredient.isBlank()) {
                Toast.makeText(this, "Please enter all the item details", Toast.LENGTH_SHORT).show()
            } else {
                updateData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()

            }
        }

        binding.txtSelectImage.setOnClickListener {
            pickImage.launch("image/*")
        }
//        binding.txtSelectImage.setOnClickListener {
//            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//        }


    }

    private fun updateData() {
        // get a reference to the "menu" node in the database
        val menuRef = databaseRef.getReference("menu")

        //Generate unique key for the new menu item
        val newItemKey = menuRef.push().key

        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imgRef: StorageReference = storageRef.child("menu_image${newItemKey}.jpg")
            val uploadTask = imgRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imgRef.downloadUrl.addOnSuccessListener {
                    //checking whether it downloaded or not
                        downloadUri ->
                    //Create a new menu item
                    val newItem = AllMenu(
                        newItemKey,
                        foodName = foodItemName,
                        foodPrice = foodPrice,
                        foodDesc = foodDesc,
                        foodImage = downloadUri.toString(),
                        foodIngredient = foodIngredient
                    )

                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to  Uploaded", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    //    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
//            uri ->
//        if (uri != null){
//            binding.imgItem.setImageURI(uri)
//        }
//    }
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.imgItem.setImageURI(uri)
            foodImageUri = uri
        }
    }
}