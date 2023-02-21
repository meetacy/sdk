package app.meetacy.sdk.exception

import io.ktor.utils.io.errors.*

class InternalServerException : RuntimeException("Server has responded with invalid response")
class InternetConnectionException(cause: IOException) : RuntimeException(cause)
