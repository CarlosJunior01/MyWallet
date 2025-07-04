package com.carlosmagno.mywallet.presentation.history

import com.carlosmagno.mywallet.data.local.TransactionEntity
import com.carlosmagno.mywallet.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionHistoryViewModelTest {

    private lateinit var viewModel: TransactionHistoryViewModel
    private lateinit var transactionRepository: TransactionRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        transactionRepository = mockk()
        viewModel = TransactionHistoryViewModel(transactionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadTransactions should filter transactions by userId`() = runTest {
        val userId = 1

        val transactions = listOf(
            TransactionEntity(1, 1, 2, 50.0, 123456789),
            TransactionEntity(2, 3, 1, 75.0, 123456790),
            TransactionEntity(3, 3, 4, 20.0, 123456791)
        )

        coEvery { transactionRepository.getAll() } returns transactions

        viewModel.loadTransactions(userId)

        advanceUntilIdle()

        val result = viewModel.transactions.value
        assertEquals(2, result.size)
        assertTrue(result.all { it.fromUserId == userId || it.toUserId == userId })
    }
}