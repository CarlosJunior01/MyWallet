package com.carlosmagno.mywallet.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.carlosmagno.mywallet.databinding.ActivityLoginBinding
import com.carlosmagno.mywallet.presentation.dashboard.DashboardActivity
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is LoginViewModel.LoginState.Success -> {
                        Toast.makeText(this@LoginActivity, "Login realizado!", Toast.LENGTH_SHORT).show()
                        binding.progressScreen.visibility = View.GONE
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("USER_ID", state.user.id)
                        startActivity(intent)
                        finish()
                    }
                    is LoginViewModel.LoginState.Error -> {
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                        binding.progressScreen.visibility = View.GONE
                    }
                    is LoginViewModel.LoginState.Loading -> {
                        binding.progressScreen.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }
}