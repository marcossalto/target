package com.marcossalto.targetmvd.util.extensions

import com.google.gson.Gson
import com.marcossalto.targetmvd.network.models.ErrorModel
import com.marcossalto.targetmvd.network.models.UnprocessableEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class ActionCallback {

    companion object {

        suspend fun <T> call(apiCall: Call<T>): Result<Data<T>> =
            withContext(Dispatchers.IO) {
                val response = apiCall.execute()
                handleResponse(response)
            }

        private fun <T> handleResponse(response: Response<T>): Result<Data<T>> {
            if (response.isSuccessful) {
                return Result.success(
                    Data(response.body())
                )
            } else {
                try {
                    var errorMessage = ""

                    response.errorBody()?.let {
                        when(response.code()){
                            401 -> {
                                val apiError = Gson().fromJson(it.charStream(), ErrorModel::class.java)
                                errorMessage = apiError.errors[0]
                            }
                            400 -> {
                                errorMessage = "Bad Request"
                            }
                            422 -> {
                                errorMessage = "Unprocessable Entity"
                                val apiError = Gson().fromJson(it.charStream(), UnprocessableEntity::class.java)
                                errorMessage = apiError.errors.full_messages.joinToString()
                            }
                            500 -> {
                                errorMessage = "Internal Server Error"
                            }
                            else -> {
                                errorMessage = "Unknown Error"
                            }
                        }
                        return Result.failure(
                            ApiException(
                                errorMessage = errorMessage
                            )
                        )
                    }
                } catch (ignore: Exception) {
                }
            }

            return Result.failure(ApiException(errorType = ApiErrorType.UNKNOWN_ERROR))
        }
    }
}

class Data<T>(val value: T?)

class ApiException(
    private val errorMessage: String? = null,
    val errorType: ApiErrorType = ApiErrorType.API_ERROR
) : java.lang.Exception() {
    override val message: String?
        get() = errorMessage
}

enum class ApiErrorType {
    API_ERROR,
    UNKNOWN_ERROR
}
