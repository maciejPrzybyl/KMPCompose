package org.macpry.kmpcompose.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.macpry.kmpcompose.repositories.CurrentUser
import org.macpry.kmpcompose.repositories.IUserRepository

class UserViewModel(
    private val userRepository: IUserRepository
) : ViewModel() {

    val userState = userRepository.currentUserFlow.map {
        UserState(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserState(null)
    )
}

data class UserState(
    val currentUser: CurrentUser?
)
