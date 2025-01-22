package org.macpry.kmpcompose.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface IUserRepository {
    val currentUserFlow: Flow<CurrentUser?>
}

class UserRepository : IUserRepository {
    override val currentUserFlow: Flow<CurrentUser?> = flowOf(null)
}

data class CurrentUser(
    val name: String,
    val email: String
)
