package com.marcossalto.targetmvd.network.managers

import androidx.annotation.RestrictTo
import com.marcossalto.targetmvd.network.models.*
import com.marcossalto.targetmvd.network.providers.ServiceProvider
import com.marcossalto.targetmvd.network.services.ApiService
import com.marcossalto.targetmvd.util.extensions.ActionCallback
import com.marcossalto.targetmvd.util.extensions.Data

/**
 * Singleton Object
 * */
object UserManager : IUserManager {

    private var service = ServiceProvider.create(ApiService::class.java)

    override suspend fun signUp(user: UserSignUp): Result<Data<UserSerializer>> =
        ActionCallback.call(service.signUp(UserSignUpSerializer(user)))

    override suspend fun signIn(user: UserSignIn): Result<Data<UserSignInResponseSerializer>> =
        ActionCallback.call(service.signIn(UserSignInSerializer(user)))

    override suspend fun signOut(): Result<Data<Void>> =
        ActionCallback.call(service.signOut())

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun reloadService(url: String) {
        service = ServiceProvider.create(ApiService::class.java, url)
    }
}
