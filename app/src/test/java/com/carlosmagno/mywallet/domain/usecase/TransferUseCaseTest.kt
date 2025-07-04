package com.carlosmagno.mywallet.domain.usecase

import com.carlosmagno.mywallet.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class TransferUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var accountRepository: AccountRepository
    private lateinit var transferUseCase: TransferUseCase

    @Before
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        transferUseCase = TransferUseCase(accountRepository)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns true when transfer succeeds`() = runTest {
        val fromId = 1
        val toId = 2
        val amount = 100.0

        `when`(accountRepository.transfer(fromId, toId, amount)).thenReturn(true)

        val result = transferUseCase(fromId, toId, amount)

        assertTrue(result)
        verify(accountRepository).transfer(fromId, toId, amount)
    }

    @Test
    fun `invoke returns false when transfer fails`() = runTest {
        val fromId = 1
        val toId = 1
        val amount = 100.0

        `when`(accountRepository.transfer(fromId, toId, amount)).thenReturn(false)

        val result = transferUseCase(fromId, toId, amount)

        assertFalse(result)
        verify(accountRepository).transfer(fromId, toId, amount)
    }
}
