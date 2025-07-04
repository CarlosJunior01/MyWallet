package com.carlosmagno.mywallet.presentation.transfer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

        loggedUserId = intent.getIntExtra("USER_ID", 0)

        binding.btnTransfer.setOnClickListener {
            val recipientEmail = binding.inputRecipient.text.toString()
            val amountText = binding.inputAmount.text.toString()
            val amount = amountText.toDoubleOrNull()

            if (recipientEmail.isBlank() || amount == null || amount <= 0) {
                Toast.makeText(this, "Preencha os campos corretamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.transfer(loggedUserId, recipientEmail, amount)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.transferState.collectLatest { state ->
                when (state) {
                    is TransactionViewModel.TransferState.Loading -> {
                    }
                    is TransactionViewModel.TransferState.Success -> {
                        Toast.makeText(this@TransactionActivity, "Transferência realizada!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is TransactionViewModel.TransferState.Error -> {
                        Toast.makeText(this@TransactionActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}
