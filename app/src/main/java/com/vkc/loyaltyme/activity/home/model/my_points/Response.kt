package com.vkc.loyaltyme.activity.home.model.my_points

data class Response(
    val gift_status: String,
    val imei_no: String,
    val loyality_point: String,
    val status: String,
    val to_retailer: Int,
    val to_subdealer: Int
)