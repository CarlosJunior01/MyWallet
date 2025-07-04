package com.carlosmagno.mywallet.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.carlosmagno.mywallet.R
import com.carlosmagno.mywallet.databinding.ActivityDashboardBinding
import com.carlosmagno.mywallet.presentation.history.TransactionHistoryActivity
import com.carlosmagno.mywallet.presentation.transfer.TransactionActivity
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModel()

    private var loggedUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupExtras()
        setupListeners()
        setupViewModel()
    }

    private fun setupExtras() {
        loggedUserId = intent.getIntExtra(USER_ID, -1)
        if (loggedUserId == -1) {
            Toast.makeText(this, getString(R.string.user_not_register), Toast.LENGTH_LONG).show()
            finish()
            return
        }
    }

    private fun setupListeners() {
        binding.btnExtract.setOnClickListener {
            val intent = Intent(this, TransactionHistoryActivity::class.java)
            intent.putExtra(USER_ID, loggedUserId)
            startActivity(intent)
        }

        binding.btnTransfer.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            intent.putExtra(USER_ID, loggedUserId)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        viewModel.loadDashboardData(loggedUserId)

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                binding.txtBalance.text = "Saldo: R$ %.2f".format(state.balance)
                binding.txtTotalTransacoes.text = "Transações: ${state.totalTransactions}"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDashboardData(loggedUserId)
    }

    companion object {
        private const val USER_ID = "USER_ID"
    }
}
