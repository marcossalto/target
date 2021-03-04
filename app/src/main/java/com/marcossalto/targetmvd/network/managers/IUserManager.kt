package com.marcossalto.targetmvd.network.managers

import com.marcossalto.targetmvd.network.models.*
import com.marcossalto.targetmvd.util.extensions.Data

interface IUserManager {
    suspend fun signUp(user: UserSignUp): Result<Data<User>>
    suspend fun signIn(user: UserSignIn): Result<Data<UserSignInResponseSerializer>>
    suspend fun signOut(): Result<Data<Void>>
    suspend fun login(accessToken: AccessTokenSerializer): Result<Data<UserSignInResponseSerializer>>
}
