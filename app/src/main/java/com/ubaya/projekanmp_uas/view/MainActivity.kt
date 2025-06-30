package com.ubaya.projekanmp_uas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.SignInActivity
import com.ubaya.projekanmp_uas.databinding.ActivityMainBinding
import com.ubaya.projekanmp_uas.session.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        if (sessionManager.get() == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.navHostMain.id) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

//        val logoutBtn = findViewById<Button>(R.id.buttonLogout)
//        logoutBtn.setOnClickListener {
//            sessionManager.clear()
//            startActivity(Intent(this, AuthActivity::class.java))
//            finish()
//        }
    }


}