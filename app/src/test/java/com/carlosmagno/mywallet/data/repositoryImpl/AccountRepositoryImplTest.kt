package com.carlosmagno.mywallet.data.repositoryImpl

import com.carlosmagno.mywallet.data.local.AccountDao
import com.carlosmagno.mywallet.data.local.AccountEntity
import com.carlosmagno.mywallet.data.local.TransactionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AccountRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var accountDao: AccountDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var accountRepository: AccountRepositoryImpl

    @Before
    fun setup() {
        accountDao = mock(AccountDao::class.java)
        transactionDao = mock(TransactionDao::class.java)
        accountRepository = AccountRepositoryImpl(accountDao, transactionDao)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAccountByUserId returns AccountEntity when found`() = runTest {
        val userId = 1
        val account = AccountEntity(userId = userId, balance = 100.0)

        whenever(accountDao.getAccount(userId)).thenReturn(account)

        val result = accountRepository.getAccountByUserId(userId)

        assertNotNull(result)
        assertEquals(account.userId, result?.userId)
        assertEquals(account.balance, result?.balance?: 0.0, 0.0)
    }

    @Test
    fun `getAccountByUserId returns null when not found`() = runTest {
        whenever(accountDao.getAccount(anyInt())).thenReturn(null)

        val result = accountRepository.getAccountByUserId(1)

        assertNull(result)
    }

    @Test
    fun `updateAccount calls accountDao updateAccount`() = runTest {
        val account = AccountEntity(userId = 1, balance = 50.0)

        accountRepository.updateAccount(account)

        verify(accountDao).updateAccount(account)
    }

    @Test
    fun `createAccount calls accountDao insertAccount`() = runTest {
        val account = AccountEntity(userId = 1, balance = 0.0)

        accountRepository.createAccount(account)

        verify(accountDao).insertAccount(account)
    }

    @Test
    fun `transfer returns false when fromUserId equals toUserId`() = runTest {
        val result = accountRepository.transfer(1, 1, 100.0)

        assertFalse(result)
        verifyNoInteractions(accountDao)
        verifyNoInteractions(transactionDao)
    }

    @Test
    fun `transfer returns false when fromAccount is null`() = runTest {
        whenever(accountDao.getAccount(1)).thenReturn(null)

        val result = accountRepository.transfer(1, 2, 50.0)

        assertFalse(result)
        verify(accountDao).getAccount(1)
        verify(accountDao).getAccount(2)
        verifyNoMoreInteractions(accountDao)
        verifyNoInteractions(transactionDao)
    }

    @Test
    fun `transfer returns false when toAccount is null`() = runTest {
        val fromAccount = AccountEntity(userId = 1, balance = 100.0)
        whenever(accountDao.getAccount(1)).thenReturn(fromAccount)
        whenever(accountDao.getAccount(2)).thenReturn(null)

        val result = accountRepository.transfer(1, 2, 50.0)

        assertFalse(result)
        verify(accountDao).getAccount(1)
        verify(accountDao).getAccount(2)
        verifyNoMoreInteractions(accountDao)
        verifyNoInteractions(transactionDao)
    }

    @Test
    fun `transfer returns false when balance is insufficient`() = runTest {
        val fromAccount = AccountEntity(userId = 1, balance = 30.0)
        val toAccount = AccountEntity(userId = 2, balance = 70.0)
        whenever(accountDao.getAccount(1)).thenReturn(fromAccount)
        whenever(accountDao.getAccount(2)).thenReturn(toAccount)

        val result = accountRepository.transfer(1, 2, 50.0)

        assertFalse(result)
        verify(accountDao).getAccount(1)
        verify(accountDao).getAccount(2)
        verifyNoMoreInteractions(accountDao)
        verifyNoInteractions(transactionDao)
    }

    @Test
    fun `transfer updates accounts and inserts transaction when successful`() = runTest {
        val fromAccount = AccountEntity(userId = 1, balance = 200.0)
        val toAccount = AccountEntity(userId = 2, balance = 100.0)
        val amount = 50.0

        whenever(accountDao.getAccount(1)).thenReturn(fromAccount)
        whenever(accountDao.getAccount(2)).thenReturn(toAccount)

        val result = accountRepository.transfer(1, 2, amount)

        assertTrue(result)

        val updatedFrom = fromAccount.copy(balance = fromAccount.balance - amount)
        val updatedTo = toAccount.copy(balance = toAccount.balance + amount)

        verify(accountDao).updateAccount(updatedFrom)
        verify(accountDao).updateAccount(updatedTo)
    }
}
