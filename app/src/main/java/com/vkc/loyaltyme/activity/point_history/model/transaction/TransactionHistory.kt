package com.vkc.loyaltyme.activity.point_history.model.transaction

data class TransactionHistory(
    var details: ArrayList<IndividualTransaction>,
    var to_name: String,
    var tot_points: String
)
