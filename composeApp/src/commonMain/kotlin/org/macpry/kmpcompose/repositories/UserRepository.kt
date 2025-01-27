package org.macpry.kmpcompose.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface IUserRepository {
    suspend fun setCurrentUser(currentUser: CurrentUser)
    val currentUserFlow: Flow<CurrentUser?>
}

class UserRepository : IUserRepository {

    private val userFlow = MutableStateFlow<CurrentUser?>(null)

    override suspend fun setCurrentUser(currentUser: CurrentUser) {
        userFlow.emit(currentUser)
    }
    override val currentUserFlow: Flow<CurrentUser?> = userFlow
}

data class CurrentUser(
    val name: String,
    val email: String,
    val photoURL: String?
)
