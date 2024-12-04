package com.example.adminqe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminqe.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding? = null
    private val  binding get() = _binding!!

    private lateinit var email :String
    private lateinit var password : String
    private lateinit var mAuth : FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("906637558272-01mda47elmsv81lqlt03angvv9td0dnd.apps.googleusercontent.com").requestEmail().build()

        mAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOption)

        binding.txtCreateAccount.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            email = binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextTextPassword.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please Enter the required field", Toast.LENGTH_SHORT).show()
            }else {
                startLogin(email,password)
            }
        }

        binding.btnGoogle.setOnClickListener {
            val signIntent : Intent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
    }

    private fun startLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Login Successfully",Toast.LENGTH_SHORT).show()
                val admin : FirebaseUser? = mAuth.currentUser
                updateUi(admin)
            }
        }.addOnFailureListener(this) {exception ->
            Toast.makeText(this,exception.message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUi(admin: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task : Task<GoogleSignInAccount> =GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount =task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)

                mAuth.signInWithCredential(credential).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this,"Google Sign-in Successfully",Toast.LENGTH_SHORT).show()
                        updateUi(null)
                    } else {
                        Toast.makeText(this,"Google Sign-in Failed",Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener(this) {exception ->
                    Toast.makeText(this,exception.message,Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this,"Google Sign-in Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }

}