package com.vkc.loyaltyme.activity.point_history

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.vkc.loyaltyapp.util.CustomToast
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.activity.point_history.adapter.TransactionHistoryAdapter
import com.vkc.loyaltyme.activity.point_history.model.transaction.Data
import com.vkc.loyaltyme.activity.point_history.model.transaction.Details
import com.vkc.loyaltyme.activity.point_history.model.transaction.Response
import com.vkc.loyaltyme.activity.point_history.model.transaction.TransactionModel
import com.vkc.loyaltyme.api.ApiClient
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
    lateinit var textTransffered: TextView
    lateinit var textDealerCount:TextView
    lateinit var textCredit:TextView
    lateinit var textDebit:TextView
    lateinit var textEarnedPoint:TextView
    lateinit var textTransferred:TextView
    lateinit var textBalance:TextView
    lateinit var editUserName: EditText
    lateinit var editPassword:EditText
    lateinit var imeiNo: String
    lateinit var historyType: String
    lateinit var listHistory: List<TransactionModel>
    lateinit var listViewHistory: ExpandableListView
    lateinit var headermanager: HeaderManager
    lateinit var relativeHeader: LinearLayout
    lateinit var llTransaction:LinearLayout
    lateinit var llRetailer:LinearLayout
    lateinit var llSubDealer:LinearLayout
    lateinit var mImageBack: ImageView
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
        listViewHistory = findViewById<View>(R.id.listViewHistory) as ExpandableListView
        relativeHeader = findViewById<View>(R.id.header) as LinearLayout
        llTransaction = findViewById<View>(R.id.llTransactionType) as LinearLayout
        llRetailer = findViewById<View>(R.id.llRetailer) as LinearLayout
        llSubDealer = findViewById<View>(R.id.llSubDealer) as LinearLayout
        textTransffered = findViewById<View>(R.id.textEarned) as TextView
        textEarnedPoint = findViewById<View>(R.id.textEarnedPoints) as TextView
        textTransferred = findViewById<View>(R.id.textTransferred) as TextView
        textBalance = findViewById<View>(R.id.textBalance) as TextView
        textDealerCount = findViewById<View>(R.id.textDealerCount) as TextView
        textCredit = findViewById<View>(R.id.textCredit) as TextView
        textDebit = findViewById<View>(R.id.textDebit) as TextView
        progressBarDialog = ProgressBarDialog(context)
        headermanager =
            HeaderManager(this@PointHistoryActivity, resources.getString(R.string.point_history))
        headermanager.getHeader(relativeHeader, 1)
        mImageBack = headermanager.leftButton!!
        headermanager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        mImageBack.setOnClickListener {
            finish()
        }
        textDebit.setOnClickListener {
            historyType = "DEBIT"

            val adapter = TransactionHistoryAdapter(
                context, listHistory
            )
            listViewHistory.setAdapter(adapter)
            textCredit.setBackgroundResource(R.drawable.rounded_rect_redline)
            textDebit.setBackgroundResource(R.drawable.rounded_rect_green)
            getHistory()
        }
        textCredit.setOnClickListener {
            historyType = "CREDIT"
            textCredit.setBackgroundResource(R.drawable.rounded_rect_green)
            textDebit.setBackgroundResource(R.drawable.rounded_rect_redline)
            val adapter = TransactionHistoryAdapter(
                context, listHistory
            )
            listViewHistory.setAdapter(adapter)
            getHistory()
        }
        listViewHistory.setOnGroupExpandListener { groupPosition ->
            if (lastExpandedPosition != -1
                && groupPosition != lastExpandedPosition
            ) {
                listViewHistory.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = groupPosition
        }
        if (PreferenceManager.getUserType(context).equals("7")) {
            llTransaction.visibility = View.VISIBLE
            llRetailer.visibility = View.GONE
            llSubDealer.visibility = View.VISIBLE
            historyType = "CREDIT"
            getHistory()
        } else {
            llTransaction.visibility = View.GONE
            llRetailer.visibility = View.VISIBLE
            llSubDealer.visibility = View.GONE
            historyType = ""
            getHistory()
        }
    }

    private fun getHistory() {
        var transactionMainResponse: TransactionModel
        var transactionResponse: Response
        var transactionData: ArrayList<Data> = ArrayList()
        var transactionDetails: Details
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            ApiClient.getApiService().getTransactionHistoryResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context),
                historyType
            ).enqueue(object : Callback<TransactionModel>{
                override fun onResponse(
                    call: Call<TransactionModel>,
                    response: retrofit2.Response<TransactionModel>
                ) {
                    progressBarDialog.hide()
                    transactionMainResponse = response.body()!!
                    transactionResponse = transactionMainResponse.response
                    Log.e("transactionresponse",transactionResponse.toString())
                    textCredit.text = transactionResponse.total_credits
                    textEarnedPoint.text = transactionResponse.total_credits
                    textTransffered.text = transactionResponse.total_debits
                    textBalance.text = transactionResponse.balance_point
                    if (transactionResponse.status.equals("Success")){
                        if (transactionResponse.data.size > 0){
                            for (i in transactionResponse.data.indices) {
                                transactionData.add(transactionResponse.data[i])
                            }
                            for (i in transactionData.indices){
                                /**** Here ****/
                            }
                        }
                    }else{

                    }
                }

                override fun onFailure(call: Call<TransactionModel>, t: Throwable) {
                    progressBarDialog.hide()
                }
            })
        }else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
    }
}