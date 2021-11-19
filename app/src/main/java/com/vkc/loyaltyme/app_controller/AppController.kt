package com.vkc.loyaltyme.app_controller

import android.app.Application
import com.vkc.loyaltyme.activity.dealers.model.dealers.Data
import com.vkc.loyaltyme.activity.point_history.model.transaction.IndividualTransaction
import com.vkc.loyaltyme.activity.point_history.model.transaction.TransactionHistory


class AppController : Application() {



    companion object {
        var instance: AppController? = null
        var dealersList: ArrayList<Data> = ArrayList()
        var notificationsList: ArrayList<com.vkc.loyaltyme.activity.inbox.model.notification.Data> = ArrayList()
        var transactionData: ArrayList<TransactionHistory> = ArrayList()
        var transactionDetails: ArrayList<IndividualTransaction> = ArrayList()

        fun applicationContext() : AppController {
            return instance as AppController
        }
    }
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }
}

