package org.macpry.kmpcompose.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.macpry.kmpcompose.managers.IAuthManager
import org.macpry.kmpcompose.repositories.CurrentUser
import org.macpry.kmpcompose.repositories.IUserRepository

class UserViewModel(
    private val userRepository: IUserRepository,
    private val authManager: IAuthManager
) : ViewModel() {

    val userState = userRepository.currentUserFlow.map {
        UserState(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserState(null)
    )

    internal fun signIn() = viewModelScope.launch {
        authManager.signIn().onSuccess {
            userRepository.setCurrentUser(it)
        }.onFailure {
            println(it)
        }
    }
}

data class UserState(
    val currentUser: CurrentUser?
)
