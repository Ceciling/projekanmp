package com.ubaya.projekanmp_uas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.SignInActivity
import com.ubaya.projekanmp_uas.session.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)

        // Cek apakah user sudah login
        val username = sessionManager.get()
        if (username == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        } else {
            println("User sedang login sebagai: $username")
        }

        val logoutBtn = findViewById<Button>(R.id.buttonLogout)
        logoutBtn.setOnClickListener {
            sessionManager.clear()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

    }
}