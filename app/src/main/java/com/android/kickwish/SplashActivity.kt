package com.android.kickwish

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.splash_activity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkLogin()
    }

    private fun checkLogin() {
        val isLoggedIn = getSharedPreferences("KickWishPrefs", MODE_PRIVATE)
            .getBoolean("isLoggedIn", false)

        val nextActivity = if (isLoggedIn) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, RegisterActivity::class.java)
        }

        startActivity(nextActivity)
        finish()
    }
}
