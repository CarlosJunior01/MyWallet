package com.carlosmagno.mywallet.presentation.auth

import app.cash.turbine.test
import com.carlosmagno.mywallet.data.local.AccountEntity
import com.carlosmagno.mywallet.domain.model.User
import com.carlosmagno.mywallet.domain.repository.AccountRepository
import com.carlosmagno.mywallet.domain.repository.UserRepository
import com.carlosmagno.mywallet.utils.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var userRepository: UserRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mock()
        accountRepository = mock()
        viewModel = RegisterViewModel(userRepository, accountRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register emits Success when registration is successful`() = runTest {
        val name = "Test User"
        val email = "test@example.com"
        val password = "1234"
        val userId = 1
        val registeredUser = User(id = userId, name = name, email = email)

        whenever(userRepository.register(any(), eq(password))).thenReturn(true)
        whenever(userRepository.login(email, password)).thenReturn(registeredUser)
        whenever(accountRepository.createAccount(any())).then {}

        viewModel.registerState.test {
            viewModel.register(name, email, password)
            advanceUntilIdle()

            assert(awaitItem() is RegisterViewModel.RegisterState.Idle)
            assert(awaitItem() is RegisterViewModel.RegisterState.Loading)
            assert(awaitItem() is RegisterViewModel.RegisterState.Success)

            cancelAndIgnoreRemainingEvents()
        }

        val accountCaptor = argumentCaptor<AccountEntity>()
        verify(accountRepository).createAccount(accountCaptor.capture())
        assert(accountCaptor.firstValue.userId == userId)
        assert(accountCaptor.firstValue.balance == 1000.0)
    }

    @Test
    fun `register emits Error when registration fails`() = runTest {
        val name = "Test User"
        val email = "test@example.com"
        val password = "1234"

        whenever(userRepository.register(any(), eq(password))).thenReturn(false)

        viewModel.registerState.test {
            viewModel.register(name, email, password)
            advanceUntilIdle()

            assert(awaitItem() is RegisterViewModel.RegisterState.Idle)
            assert(awaitItem() is RegisterViewModel.RegisterState.Loading)
            val errorState = awaitItem() as RegisterViewModel.RegisterState.Error
            assert(errorState.message == "Erro ao registrar usuário")

            cancelAndIgnoreRemainingEvents()
        }

        verify(accountRepository, never()).createAccount(any())
    }
}
