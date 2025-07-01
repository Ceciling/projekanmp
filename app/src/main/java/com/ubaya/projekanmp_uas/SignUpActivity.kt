package com.ubaya.projekanmp_uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ubaya.projekanmp_uas.databinding.ActivitySignUpBinding
import com.ubaya.projekanmp_uas.model.entity.User
import com.ubaya.projekanmp_uas.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAkunButton.setOnClickListener {
            val username = binding.editTextUsername.text.toString().trim()
            val firstName = binding.editTextFirstName.text.toString().trim()
            val lastName = binding.editTextLastName.text.toString().trim()
            val password = binding.editTextPassword2.text.toString().trim()
            val repeatPassword = binding.editTextRepeatPassword.text.toString().trim()

            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                Toast.makeText(this, "Password dan Repeat Password tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(username, firstName, lastName, password)
            userViewModel.register(user)
        }

        userViewModel.registerSuccessLD.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }

        userViewModel.errorLD.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}