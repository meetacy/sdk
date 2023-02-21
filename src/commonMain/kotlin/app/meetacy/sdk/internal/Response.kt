package app.meetacy.sdk.internal

import app.meetacy.sdk.exception.InternalServerException

internal sealed interface Response<out T> {
    data class Success<T>(val result: T) : Response<T>
    data class Error(val errorCode: Int) : Response<Nothing> {
        fun unknownErrorCode(): Nothing = throw InternalServerException()
    }
}
