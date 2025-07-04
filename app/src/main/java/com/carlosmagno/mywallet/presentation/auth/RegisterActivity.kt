package com.carlosmagno.mywallet.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.carlosmagno.mywallet.R
import com.carlosmagno.mywallet.databinding.ActivityRegisterBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : ComponentActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        observeRegisterState()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.field_generic_error), Toast.LENGTH_SHORT).show()
            } else {
                registerViewModel.register(name, email, password)
            }
        }

        binding.btnCancel.setOnClickListener { finish() }
    }

    private fun observeRegisterState() {
        lifecycleScope.launchWhenStarted {
            registerViewModel.registerState.collectLatest { state ->
                when (state) {
                    is RegisterViewModel.RegisterState.Idle -> {
                        binding.progressLoading.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                    }
                    is RegisterViewModel.RegisterState.Loading -> {
                        binding.progressLoading.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                    }
                    is RegisterViewModel.RegisterState.Success -> {
                        binding.progressLoading.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity,
                            getString(R.string.register_app_success), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is RegisterViewModel.RegisterState.Error -> {
                        binding.progressLoading.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                    }
                }
            }
        }
    }
}
