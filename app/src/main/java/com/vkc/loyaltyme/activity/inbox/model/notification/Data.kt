package com.vkc.loyaltyme.activity.inbox.model.notification

data class Data(
    var createdon: String,
    var from_date: String,
    var image: String,
    var message: String,
    val notification_type: String,
    val pdf: String,
    var title: String,
    var to_date: String
)