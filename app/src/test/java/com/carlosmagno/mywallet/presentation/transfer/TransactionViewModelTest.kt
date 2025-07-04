package com.carlosmagno.mywallet.presentation.transfer

import com.carlosmagno.mywallet.domain.model.User
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.UserRepository
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {

    private lateinit var viewModel: TransactionViewModel
    private val accountRepository: AccountRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TransactionViewModel(accountRepository, userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `transfer should emit Success when the transfer is successful`() = runTest {
        val fromUserId = 1
        val toEmail = "destino@teste.com"
        val amount = 100.0

        val toUser = User(id = 2, name = "Destino", email = toEmail)

        coEvery { userRepository.getUserByEmail(toEmail) } returns toUser
        coEvery { accountRepository.transfer(fromUserId, toUser.id, amount) } returns true

        viewModel.transfer(fromUserId, toEmail, amount)
        advanceUntilIdle()

        assert(viewModel.transferState.value is TransactionViewModel.TransferState.Success)
    }

    @Test
    fun `transfer should throw Error when the recipient user is not found`() = runTest {
        val fromUserId = 1
        val toEmail = "naoencontrado@teste.com"
        val amount = 50.0

        coEvery { userRepository.getUserByEmail(toEmail) } returns null

        viewModel.transfer(fromUserId, toEmail, amount)
        advanceUntilIdle()

        val state = viewModel.transferState.value
        assert(state is TransactionViewModel.TransferState.Error)
        assert((state as TransactionViewModel.TransferState.Error).message.contains("não encontrado"))
    }

    @Test
    fun `transfer should throw Error when transfer fails`() = runTest {
        val fromUserId = 1
        val toEmail = "erro@teste.com"
        val amount = 200.0
        val toUser = User(id = 2, name = "Destino", email = toEmail)

        coEvery { userRepository.getUserByEmail(toEmail) } returns toUser
        coEvery { accountRepository.transfer(fromUserId, toUser.id, amount) } returns false

        viewModel.transfer(fromUserId, toEmail, amount)
        advanceUntilIdle()

        val state = viewModel.transferState.value
        assert(state is TransactionViewModel.TransferState.Error)
        assert((state as TransactionViewModel.TransferState.Error).message.contains("Falha"))
    }
}
