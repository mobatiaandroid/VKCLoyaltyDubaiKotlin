package com.vkc.loyaltyme.activity.point_history

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.vkc.loyaltyme.utils.CustomToast
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.activity.point_history.adapter.TransactionHistoryAdapter
import com.vkc.loyaltyme.activity.point_history.adapter.TransactionHistoryAdapterNew
import com.vkc.loyaltyme.activity.point_history.model.transaction.Response
import com.vkc.loyaltyme.activity.point_history.model.transaction.TransactionModel
import com.vkc.loyaltyme.activity.point_history.model.transaction_new.TransactionModelNew
import com.vkc.loyaltyme.api.ApiClient
import com.vkc.loyaltyme.app_controller.AppController
import com.vkc.loyaltyme.manager.HeaderManager
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.ProgressBarDialog
import com.vkc.loyaltyme.utils.UtilityMethods
import retrofit2.Call
import retrofit2.Callback
import kotlin.collections.ArrayList

class PointHistoryActivity : AppCompatActivity() {
    lateinit var context: Activity
    lateinit var buttonLogin: Button
    lateinit var textEarnedPointRetailer: TextView
    lateinit var textDealerCount:TextView
    lateinit var textCredit:TextView
    lateinit var textDebit:TextView
    lateinit var textEarnedPointSubDealer:TextView
    lateinit var textTransferredSubDealer:TextView
    lateinit var textBalance:TextView
    lateinit var editUserName: EditText
    lateinit var editPassword:EditText
    lateinit var imeiNo: String
    lateinit var historyType: String
    lateinit var listHistory: List<TransactionModel>

    //    lateinit var listViewHistory: ExpandableListView
    lateinit var recyclerHistory: RecyclerView
    lateinit var headerManager: HeaderManager
    lateinit var header: LinearLayout
    lateinit var llTransaction:LinearLayout
    lateinit var llRetailer:LinearLayout
    lateinit var llSubDealer:LinearLayout
    lateinit var imageBack: ImageView
    lateinit var progressBarDialog: ProgressBarDialog
    private var lastExpandedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_history)
        context = this
        initialiseUI()

    }

    private fun initialiseUI() {
        listHistory = ArrayList<TransactionModel>()
//        listViewHistory = findViewById<View>(R.id.listViewHistory) as ExpandableListView
        recyclerHistory = findViewById<View>(R.id.recyclerHistory) as RecyclerView
        header = findViewById<View>(R.id.header) as LinearLayout
        llTransaction = findViewById<View>(R.id.llTransactionType) as LinearLayout
        llRetailer = findViewById<View>(R.id.llRetailer) as LinearLayout
        llSubDealer = findViewById<View>(R.id.llSubDealer) as LinearLayout
        textEarnedPointRetailer = findViewById<View>(R.id.textEarnedCouponsRetailer) as TextView
        textEarnedPointSubDealer = findViewById<View>(R.id.textEarnedCouponsSubDealer) as TextView
        textTransferredSubDealer =
            findViewById<View>(R.id.textTransferredCouponsSubDealer) as TextView
        textBalance = findViewById<View>(R.id.textBalance) as TextView
        textDealerCount = findViewById<View>(R.id.textDealerCountRetailer) as TextView
        textCredit = findViewById<View>(R.id.textCredit) as TextView
        textDebit = findViewById<View>(R.id.textDebit) as TextView
        progressBarDialog = ProgressBarDialog(context)
        headerManager =
            HeaderManager(this@PointHistoryActivity, resources.getString(R.string.point_history))
        headerManager.getHeader(header, 1)
        imageBack = headerManager.leftButton!!
        headerManager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        imageBack.setOnClickListener {
            val intent = Intent(context,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        textDebit.setOnClickListener {
            historyType = "DEBIT"
//            val adapter = TransactionHistoryAdapter()
            val adapter = TransactionHistoryAdapterNew()

//            listViewHistory.setAdapter(adapter)
            recyclerHistory.setAdapter(adapter)
            textCredit.setBackgroundResource(R.drawable.rounded_rect_redline)
            textDebit.setBackgroundResource(R.drawable.rounded_rect_green)
//            getHistory()
            getHistoryNew()
        }
        textCredit.setOnClickListener {
            historyType = "CREDIT"
            textCredit.setBackgroundResource(R.drawable.rounded_rect_green)
            textDebit.setBackgroundResource(R.drawable.rounded_rect_redline)
//            val adapter = TransactionHistoryAdapter()
            val adapter = TransactionHistoryAdapterNew()
//            listViewHistory.setAdapter(adapter)
            recyclerHistory.setAdapter(adapter)
//            getHistory()
            getHistoryNew()
        }
//        listViewHistory.setOnGroupExpandListener { groupPosition ->
//            if (lastExpandedPosition != -1
//                && groupPosition != lastExpandedPosition
//            ) {
//                listViewHistory.collapseGroup(lastExpandedPosition)
//            }
//            lastExpandedPosition = groupPosition
//        }
        if (PreferenceManager.getUserType(context).equals("7")) {
            llTransaction.visibility = View.VISIBLE
            llRetailer.visibility = View.GONE
            llSubDealer.visibility = View.VISIBLE
            historyType = "CREDIT"
//            getHistory()
            getHistoryNew()
        } else {
            llTransaction.visibility = View.GONE
            llRetailer.visibility = View.VISIBLE
            llSubDealer.visibility = View.GONE
            historyType = ""
//            getHistory()
            getHistoryNew()
        }
    }

    private fun getHistory() {
//        var transactionMainResponse: TransactionModel
//        var transactionResponse: Response
//        if (UtilityMethods.checkInternet(context)){
//            progressBarDialog.show()
//            ApiClient.getApiService().getTransactionHistoryResponse(
//                "23703","5",""
////                PreferenceManager.getCustomerID(context),
////                PreferenceManager.getUserType(context),
////                historyType
//            ).enqueue(object : Callback<TransactionModel>{
//                override fun onResponse(
//                    call: Call<TransactionModel>,
//                    response: retrofit2.Response<TransactionModel>
//                ) {
//                    progressBarDialog.hide()
//                    if (response.body() != null){
//                        transactionMainResponse = response.body() !!
//                        transactionResponse = transactionMainResponse.response
//                        textEarnedPointRetailer.text = transactionResponse.total_credits
//                        textEarnedPointSubDealer.text = transactionResponse.total_credits
//                        textTransferredSubDealer.text = transactionResponse.total_debits
//                        textDealerCount.text = transactionResponse.data.size.toString()
//                        textBalance.text = transactionResponse.balance_point
//                        if (transactionResponse.status.equals("Success")) {
//                            if (transactionResponse.data.size > 0) {
//                                for (i in transactionResponse.data.indices) {
//                                    AppController.transactionData.add(transactionResponse.data[i])
//                                }
//                                for (i in AppController.transactionData.indices) {
//                                    for (j in AppController.transactionData[i].details.indices) {
//                                        AppController.transactionDetails.add(AppController.transactionData[i].details[j])
//                                    }
//                                }
//                                textDealerCount.text = AppController.transactionData.size.toString()
//                                val adapter = TransactionHistoryAdapter()
//                                listViewHistory.setAdapter(adapter)
//                            }
//                        } else {
//
//                        }
//                    } else {
//                        Log.e("Error",response.code().toString())
//                    }
//                }
//
//                override fun onFailure(call: Call<TransactionModel>, t: Throwable) {
//                    progressBarDialog.hide()
//                }
//            })
//        }else{
//            CustomToast.customToast(context)
//            CustomToast.show(58)
//        }
    }

    private fun getHistoryNew() {
        var transactionMainResponse: TransactionModelNew
        var transactionResponse: TransactionModelNew.Response
        if (UtilityMethods.checkInternet(context)) {
            progressBarDialog.show()
            ApiClient.getApiService().getTransactionHistoryResponseNew(
                "23703", "5", ""
//                PreferenceManager.getCustomerID(context),
//                PreferenceManager.getUserType(context),
//                historyType
            ).enqueue(object : Callback<TransactionModelNew> {
                override fun onResponse(
                    call: Call<TransactionModelNew>,
                    response: retrofit2.Response<TransactionModelNew>,
                ) {
                    progressBarDialog.hide()
                    if (response.body() != null) {
                        transactionMainResponse = response.body() !!
                        transactionResponse = transactionMainResponse.response
                        textEarnedPointRetailer.text = transactionResponse.total_credits
                        textEarnedPointSubDealer.text = transactionResponse.total_credits
                        textTransferredSubDealer.text = transactionResponse.total_debits
                        textDealerCount.text = transactionResponse.data.size.toString()
                        textBalance.text = transactionResponse.balance_point
                        if (transactionResponse.status.equals("Success")) {
                            if (transactionResponse.data.size > 0) {
                                for (i in transactionResponse.data.indices) {
                                    AppController.transactionDataNew.add(transactionResponse.data[i])
                                }
//                                for (i in AppController.transactionData.indices) {
//                                    for (j in AppController.transactionData[i].details.indices) {
//                                        AppController.transactionDetails.add(AppController.transactionData[i].details[j])
//                                    }
//                                }
                                textDealerCount.text = AppController.transactionData.size.toString()
//                                val adapter = TransactionHistoryAdapter()
                                val adapter = TransactionHistoryAdapterNew()
//                                listViewHistory.setAdapter(adapter)
                                recyclerHistory.adapter = adapter
                            }
                        } else {

                        }

                    } else {
                        Log.e("Error", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<TransactionModelNew>, t: Throwable) {
                    progressBarDialog.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(58)
                }

            })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
    }
}