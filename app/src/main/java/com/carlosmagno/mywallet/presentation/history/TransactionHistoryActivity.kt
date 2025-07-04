package com.carlosmagno.mywallet.presentation.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosmagno.mywallet.databinding.ActivityTransactionHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionHistoryBinding
    private val viewModel: TransactionHistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTransactions.layoutManager = LinearLayoutManager(this)

        val userId = intent.getIntExtra("USER_ID", -1)

        viewModel.loadTransactions(userId)

        binding.backButton.setOnClickListener { finish() }

        lifecycleScope.launch {
            viewModel.transactions.collectLatest { list ->
                binding.rvTransactions.adapter = TransactionsAdapter(list)
            }
        }
    }
}
