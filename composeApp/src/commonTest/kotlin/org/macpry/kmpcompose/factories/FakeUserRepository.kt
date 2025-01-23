package org.macpry.kmpcompose.factories

import kotlinx.coroutines.flow.Flow
import org.macpry.kmpcompose.repositories.CurrentUser
import org.macpry.kmpcompose.repositories.IUserRepository

class FakeUserRepository(fakeUserFlow: Flow<CurrentUser?>) : IUserRepository {
    override val currentUserFlow: Flow<CurrentUser?> = fakeUserFlow
}