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
import com.ubaya.projekanmp_uas.databinding.FragmentSignUpBinding
import com.ubaya.projekanmp_uas.model.entity.User
import com.ubaya.projekanmp_uas.viewmodel.UserViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.createAkunButton.setOnClickListener {
            val username = binding.editTextUsername.text.toString().trim()
            val firstName = binding.editTextFirstName.text.toString().trim()
            val lastName = binding.editTextLastName.text.toString().trim()
            val password = binding.editTextPassword2.text.toString().trim()
            val repeatPassword = binding.editTextRepeatPassword.text.toString().trim()

            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                password.isEmpty() || repeatPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                Toast.makeText(requireContext(), "Password dan Repeat Password tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(0, username, firstName, lastName, password)
            userViewModel.register(user)
        }

        userViewModel.registerSuccessLD.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // kembali ke SignInFragment

                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                requireActivity().finish()

            }
        }

        userViewModel.errorLD.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return  binding.root
    }


}