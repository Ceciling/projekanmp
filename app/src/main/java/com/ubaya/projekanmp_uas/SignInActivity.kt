package com.ubaya.projekanmp_uas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.ubaya.projekanmp_uas.databinding.ActivitySignInBinding
import com.ubaya.projekanmp_uas.session.SessionManager
import com.ubaya.projekanmp_uas.view.MainActivity
import com.ubaya.projekanmp_uas.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val editTextUsername = findViewById<TextInputEditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<TextInputEditText>(R.id.editTextPassword)
        val signInButton = findViewById<Button>(R.id.signInButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // Aksi login saat tombol Sign In diklik
        signInButton.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                userViewModel.login(username, password)
            } else {
                Toast.makeText(this, "Username dan password wajib diisi", Toast.LENGTH_SHORT).show()
            }
        }
        sessionManager = SessionManager(this)
        // Observer hasil login
        userViewModel.userLD.observe(this) { user ->
            if (user != null) {
                sessionManager.save(user.username)
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }

        // Observer jika terjadi error saat login
        userViewModel.errorLD.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        // Aksi ke halaman SignUpActivity
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}