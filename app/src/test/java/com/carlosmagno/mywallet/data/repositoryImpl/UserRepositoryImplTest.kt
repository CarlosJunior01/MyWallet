package com.carlosmagno.mywallet.data.repositoryImpl

import com.carlosmagno.mywallet.data.local.UserDao
import com.carlosmagno.mywallet.data.local.UserEntity
import com.carlosmagno.mywallet.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setup() {
        userDao = mock(UserDao::class.java)
        userRepository = UserRepositoryImpl(userDao)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login returns User when UserEntity found`() = runTest {
        val email = "test@example.com"
        val password = "1234"
        val userEntity = UserEntity(id = 1, name = "Test User", email = email, password = password)

        whenever(userDao.login(email, password)).thenReturn(userEntity)

        val user = userRepository.login(email, password)

        assertNotNull(user)
        assertEquals(userEntity.id, user?.id)
        assertEquals(userEntity.name, user?.name)
        assertEquals(userEntity.email, user?.email)
    }

    @Test
    fun `login returns null when UserEntity not found`() = runTest {
        whenever(userDao.login(anyString(), anyString())).thenReturn(null)

        val user = userRepository.login("any", "any")

        assertNull(user)
    }

    @Test
    fun `register returns true when insert is successful`() = runTest {
        val user = User(id = 0, name = "New User", email = "new@example.com")
        val password = "pass"

        whenever(userDao.insert(any())).thenReturn(1L)

        val result = userRepository.register(user, password)

        assertTrue(result)
    }

    @Test
    fun `register returns false when insert fails`() = runTest {
        val user = User(id = 0, name = "New User", email = "new@example.com")
        val password = "pass"

        whenever(userDao.insert(any())).thenReturn(0L)

        val result = userRepository.register(user, password)

        assertFalse(result)
    }

    @Test
    fun `getUserById returns User when found`() = runTest {
        val id = 1
        val userEntity = UserEntity(id = id, name = "Found User", email = "found@example.com", password = "123")

        whenever(userDao.getUserById(id)).thenReturn(userEntity)

        val user = userRepository.getUserById(id)

        assertNotNull(user)
        assertEquals(id, user?.id)
    }

    @Test
    fun `getUserById returns null when not found`() = runTest {
        whenever(userDao.getUserById(anyInt())).thenReturn(null)

        val user = userRepository.getUserById(1)

        assertNull(user)
    }

    @Test
    fun `getUserByEmail returns User when found`() = runTest {
        val email = "found@example.com"
        val userEntity = UserEntity(id = 1, name = "Found User", email = email, password = "123")

        whenever(userDao.getUserByEmail(email)).thenReturn(userEntity)

        val user = userRepository.getUserByEmail(email)

        assertNotNull(user)
        assertEquals(email, user?.email)
    }

    @Test
    fun `getUserByEmail returns null when not found`() = runTest {
        whenever(userDao.getUserByEmail(anyString())).thenReturn(null)

        val user = userRepository.getUserByEmail("any@example.com")

        assertNull(user)
    }
}
