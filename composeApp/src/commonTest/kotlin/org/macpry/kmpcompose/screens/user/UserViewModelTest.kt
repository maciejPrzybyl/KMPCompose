package org.macpry.kmpcompose.screens.user

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.macpry.kmpcompose.factories.FakeUserRepository
import org.macpry.kmpcompose.managers.IAuthManager
import org.macpry.kmpcompose.repositories.CurrentUser
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun observeUserState() = runTest {
        val userFlow = MutableStateFlow<CurrentUser?>(null)
        val viewModel = UserViewModel(FakeUserRepository(userFlow), FakeAuthManager())

        viewModel.userState.test {
            assertEquals(UserState(null), awaitItem())

            val newUser = CurrentUser("akh", "sdfa")
            userFlow.emit(newUser)
            assertEquals(UserState(newUser), awaitItem())
        }
    }

    class FakeAuthManager : IAuthManager {
        override suspend fun signIn() {}
    }
}