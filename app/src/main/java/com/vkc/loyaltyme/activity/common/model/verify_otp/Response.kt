package com.vkc.loyaltyme.activity.common.model.verify_otp

data class Response(
    val otp_attempt: String,
    val status: String
)