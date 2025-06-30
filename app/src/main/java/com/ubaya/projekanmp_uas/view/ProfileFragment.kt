package com.ubaya.projekanmp_uas.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ubaya.projekanmp_uas.R
import com.ubaya.projekanmp_uas.databinding.FragmentProfileBinding
import com.ubaya.projekanmp_uas.session.SessionManager
import com.ubaya.projekanmp_uas.viewmodel.UserViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sessionManager = SessionManager(requireContext())
        val currentUsername = sessionManager.get()

        // SIGN OUT
        binding.buttonSignOut.setOnClickListener {
            sessionManager.clear()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // CHANGE PASSWORD
        binding.buttonChangePassword.setOnClickListener {
            val oldPassword = binding.oldPasswordEdit.text.toString().trim()
            val newPassword = binding.newPasswordEdit.text.toString().trim()
            val repeatPassword = binding.repeatPasswordEdit.text.toString().trim()

            if (oldPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != repeatPassword) {
                Toast.makeText(requireContext(), "Password baru tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentUsername != null) {
                userViewModel.validatePassword(currentUsername, oldPassword) { isValid ->
                    if (isValid) {
                        userViewModel.updatePassword(currentUsername, newPassword)
                        Toast.makeText(requireContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                        // Opsional: Clear field setelah ubah password
                        binding.oldPasswordEdit.text?.clear()
                        binding.newPasswordEdit.text?.clear()
                        binding.repeatPasswordEdit.text?.clear()
                    } else {
                        Toast.makeText(requireContext(), "Password lama salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}