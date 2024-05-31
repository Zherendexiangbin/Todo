package net.onest.time.api.utils

class ResponseErrorException(
    val code: String,
    msg: String
) : RuntimeException(msg)