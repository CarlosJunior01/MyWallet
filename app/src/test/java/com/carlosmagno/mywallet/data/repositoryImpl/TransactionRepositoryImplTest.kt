package com.carlosmagno.mywallet.data.repositoryImpl

import com.carlosmagno.mywallet.data.local.TransactionDao
import com.carlosmagno.mywallet.data.local.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var transactionDao: TransactionDao
    private lateinit var transactionRepository: TransactionRepositoryImpl

    @Before
    fun setup() {
        transactionDao = mock(TransactionDao::class.java)
        transactionRepository = TransactionRepositoryImpl(transactionDao)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insert calls transactionDao insertTransaction`() = runTest {
        val transaction = TransactionEntity(fromUserId = 1, toUserId = 2, amount = 100.0, timestamp = System.currentTimeMillis())

        transactionRepository.insert(transaction)

        verify(transactionDao).insertTransaction(transaction)
    }

    @Test
    fun `getAll returns list of transactions`() = runTest {
        val transactions = listOf(
            TransactionEntity(fromUserId = 1, toUserId = 2, amount = 50.0, timestamp = 1000L),
            TransactionEntity(fromUserId = 2, toUserId = 3, amount = 75.0, timestamp = 2000L)
        )

        `when`(transactionDao.getAllTransactions()).thenReturn(transactions)

        val result = transactionRepository.getAll()

        assertEquals(transactions, result)
    }

    @Test
    fun `getAllByUser returns list of transactions for user`() = runTest {
        val userId = 1
        val userTransactions = listOf(
            TransactionEntity(fromUserId = 1, toUserId = 2, amount = 50.0, timestamp = 1000L),
            TransactionEntity(fromUserId = 3, toUserId = 1, amount = 25.0, timestamp = 1500L)
        )

        `when`(transactionDao.getTransactionsByUser(userId)).thenReturn(userTransactions)

        val result = transactionRepository.getAllByUser(userId)

        assertEquals(userTransactions, result)
    }
}
