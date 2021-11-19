package com.vkc.loyaltyme.activity.point_history.model.transaction

data class IndividualTransaction(
    var points: String,
    var type: String,
    var to_name: String,
    var to_role: String,
    var date: String
)
