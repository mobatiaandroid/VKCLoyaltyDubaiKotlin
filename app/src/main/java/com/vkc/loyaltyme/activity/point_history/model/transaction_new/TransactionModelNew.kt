package com.vkc.loyaltyme.activity.point_history.model.transaction_new

import com.vkc.loyaltyme.activity.point_history.constants.Constants


data class TransactionModelNew(
    val response: Response,
) {
    data class Response(
        val balance_point: String,
        val data: ArrayList<TransactionHistory>,
        val status: String,
        val total_credits: String,
        val total_debits: String,
    ) {
        data class TransactionHistory(
            var details: ArrayList<IndividualTransaction>,
            var to_name: String,
            var tot_points: String,
            var type: Int = Constants.PARENT,
            var isExpanded: Boolean = false,
        ) {
            data class IndividualTransaction(
                var points: String,
                var type: String,
                var to_name: String,
                var to_role: String,
                var date: String,
            )
        }

    }
}

