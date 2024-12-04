package com.example.adminqe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.adminqe.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


    }

    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        val  user = mAuth.currentUser
        if (user != null){
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            },3000)
        }else{
            startActivity(Intent(this, SignActivity::class.java))
            finish()
        }
    }
}