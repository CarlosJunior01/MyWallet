package com.carlosmagno.mywallet.domain.usecase

import kotlinx.coroutines.Dispatchers
import com.carlosmagno.mywallet.domain.model.User
import com.carlosmagno.mywallet.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        userRepository = mock(UserRepository::class.java)
        loginUseCase = LoginUseCase(userRepository)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns User when repository returns User`() = runTest {
        val email = "test@example.com"
        val password = "1234"
        val user = User(id = 1, name = "Test User", email = email)

        `when`(userRepository.login(email, password)).thenReturn(user)

        val result = loginUseCase(email, password)

        assertNotNull(result)
        assertEquals(user.id, result?.id)
        assertEquals(user.name, result?.name)
        assertEquals(user.email, result?.email)

        verify(userRepository).login(email, password)
    }

    @Test
    fun `invoke returns null when repository returns null`() = runTest {
        val email = "test@example.com"
        val password = "wrongpass"

        `when`(userRepository.login(anyString(), anyString())).thenReturn(null)

        val result = loginUseCase(email, password)

        assertNull(result)

        verify(userRepository).login(email, password)
    }
}
