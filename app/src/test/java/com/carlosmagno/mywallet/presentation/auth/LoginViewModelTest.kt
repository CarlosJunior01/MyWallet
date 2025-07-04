package com.carlosmagno.mywallet.presentation.auth

import com.carlosmagno.mywallet.domain.model.User
import com.carlosmagno.mywallet.domain.repository.UserRepository
import com.carlosmagno.mywallet.utils.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mock(UserRepository::class.java)
        loginViewModel = LoginViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial loginState is Idle`() = runTest {
        val state = loginViewModel.loginState.first()
        assertTrue(state is LoginViewModel.LoginState.Idle)
    }

    @Test
    fun `login success updates loginState to Success`() = runTest {
        val user = User(id = 1, name = "Test User", email = "test@example.com")
        `when`(userRepository.login("test@example.com", "1234")).thenReturn(user)

        loginViewModel.login("test@example.com", "1234")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = loginViewModel.loginState.value
        assertTrue(state is LoginViewModel.LoginState.Success)
        assertEquals(user, (state as LoginViewModel.LoginState.Success).user)
    }

    @Test
    fun `login failure updates loginState to Error`() = runTest {
        `when`(userRepository.login("wrong@example.com", "wrongpass")).thenReturn(null)

        loginViewModel.login("wrong@example.com", "wrongpass")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = loginViewModel.loginState.value
        assertTrue(state is LoginViewModel.LoginState.Error)
        assertEquals("Usuário ou senha inválidos", (state as LoginViewModel.LoginState.Error).message)
    }
}
