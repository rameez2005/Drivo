package com.example.drivo.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.drivo.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("OWNER_NAME", "Ahmed Khan")       // hardcoded owner name
            intent.putExtra("COMPANY_NAME", "TransFleet Co.")
            startActivity(intent)
            finish()
        }, 2000)
    }
}

