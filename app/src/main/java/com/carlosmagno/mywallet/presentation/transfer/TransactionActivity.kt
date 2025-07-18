package com.carlosmagno.mywallet.presentation.transfer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.carlosmagno.mywallet.R
import com.carlosmagno.mywallet.databinding.ActivityTransactionBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding
    private val viewModel: TransactionViewModel by viewModel()

    private var loggedUserId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupExtras()
        setupListeners()
        setupViewModel()
    }

    private fun setupExtras() {
        loggedUserId = intent.getIntExtra(USER_ID, 0)
    }

    private fun setupListeners() {
        binding.btnTransfer.setOnClickListener {
            val recipientEmail = binding.inputRecipient.text.toString()
            val amountText = binding.inputAmount.text.toString()
            val amount = amountText.toDoubleOrNull()

            if (recipientEmail.isBlank() || amount == null || amount <= 0) {
                Toast.makeText(this,
                    getString(R.string.field_generic_correctly_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.transfer(loggedUserId, recipientEmail, amount)
        }

        binding.backButton.setOnClickListener { finish() }
    }

    private fun setupViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.transferState.collectLatest { state ->
                when (state) {
                    is TransactionViewModel.TransferState.Loading -> {
                        binding.progressScreen.visibility = View.VISIBLE
                    }
                    is TransactionViewModel.TransferState.Success -> {
                        Toast.makeText(this@TransactionActivity,
                            getString(R.string.transfer_success), Toast.LENGTH_SHORT).show()
                        binding.progressScreen.visibility = View.GONE
                        finish()
                    }
                    is TransactionViewModel.TransferState.Error -> {
                        Toast.makeText(this@TransactionActivity, state.message, Toast.LENGTH_SHORT).show()
                        binding.progressScreen.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        private const val USER_ID = "USER_ID"
    }
}
