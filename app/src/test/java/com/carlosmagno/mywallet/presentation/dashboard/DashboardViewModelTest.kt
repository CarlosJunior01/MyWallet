package com.carlosmagno.mywallet.presentation.dashboard

import com.carlosmagno.mywallet.data.local.AccountEntity
import com.carlosmagno.mywallet.data.local.TransactionEntity
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        accountRepository = mock()
        transactionRepository = mock()
        viewModel = DashboardViewModel(accountRepository, transactionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDashboardData updates uiState with balance and totalTransactions`() = runTest {
        val userId = 1
        val account = AccountEntity(userId = userId, balance = 500.0)
        val transactions = listOf(
            TransactionEntity(
                id = 1,
                fromUserId = userId,
                toUserId = 2,
                amount = 100.0,
                timestamp = System.currentTimeMillis()
            ),
            TransactionEntity(
                id = 2,
                fromUserId = userId,
                toUserId = 3,
                amount = 200.0,
                timestamp = System.currentTimeMillis()
            )
        )

        whenever(accountRepository.getAccountByUserId(userId)).thenReturn(account)
        whenever(transactionRepository.getAllByUser(userId)).thenReturn(transactions)

        viewModel.loadDashboardData(userId)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(500.0, state.balance, 0.01)
        assertEquals(2, state.totalTransactions)
    }
}
