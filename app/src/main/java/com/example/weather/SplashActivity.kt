package com.example.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({  //to delay the start of main Acitivity
                val intent = Intent(this
                ,MainActivity::class.java)
            startActivity(intent)
            finish() //to coming back our splash screen will not appear again

        },3000)
    }
}