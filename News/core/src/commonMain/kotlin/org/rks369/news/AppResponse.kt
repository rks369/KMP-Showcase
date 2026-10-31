package org.rks369.news

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

sealed class AppException(
    val title: String,
    val description: String? = null
) {

    class NoInternet() : AppException(
        title = "No Internet",
        description = "Please check your internet connection and try Again."
    )
    class Timeout() : AppException(
        title = "Time out",
        description = "Try Again"
    )
    class ServerError() : AppException(
        title = "Something happen on our end.",
        description = "Try again now or after some time"
    )
    class ParsingError() : AppException(
        title = "Something went wrong",

    )
    class Unknown() : AppException(
        title = "Something went wrong",
    )

}


fun Throwable.toAppException() : AppException {
    return when (this) {
        is HttpRequestTimeoutException,
        is ConnectTimeoutException -> AppException.Timeout()
        is ServerResponseException,
        is ClientRequestException -> AppException.ServerError()
        is SerializationException -> AppException.ParsingError()
        is IOException -> AppException.NoInternet()
        else -> AppException.Unknown()
    }
}