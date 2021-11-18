package com.vkc.loyaltyme.activity.point_history.model.transaction

data class Response(
    val balance_point: String,
    val data: ArrayList<Data>,
    val status: String,
    val total_credits: String,
    val total_debits: String
)