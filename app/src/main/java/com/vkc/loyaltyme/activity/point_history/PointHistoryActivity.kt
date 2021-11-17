package com.vkc.loyaltyme.activity.point_history

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.point_history.adapter.TransactionHistoryAdapter
import com.vkc.loyaltyme.activity.point_history.model.transaction.TransactionModel
import com.vkc.loyaltyme.manager.HeaderManager
import com.vkc.loyaltyme.manager.PreferenceManager
import java.util.ArrayList

class PointHistoryActivity : AppCompatActivity() {
    lateinit var mContext: Activity
    lateinit var buttonLogin: Button
    lateinit var textEarned: TextView
    lateinit var textDealerCount:TextView
    lateinit var textCredit:TextView
    lateinit var textDebit:TextView
    lateinit var textEarnedPoint:TextView
    lateinit var textTransferred:TextView
    lateinit var textBalance:TextView
    lateinit var mEditUserName: EditText
    lateinit var mEditPassword:EditText
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
    private var lastExpandedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_history)
        mContext = this
        initialiseUI()

    }

    private fun initialiseUI() {
        listHistory = ArrayList<TransactionModel>()
        listViewHistory = findViewById<View>(R.id.listViewHistory) as ExpandableListView
        relativeHeader = findViewById<View>(R.id.header) as LinearLayout
        llTransaction = findViewById<View>(R.id.llTransactionType) as LinearLayout
        llRetailer = findViewById<View>(R.id.llRetailer) as LinearLayout
        llSubDealer = findViewById<View>(R.id.llSubDealer) as LinearLayout
        textEarned = findViewById<View>(R.id.textEarned) as TextView
        textEarnedPoint = findViewById<View>(R.id.textEarnedPoints) as TextView
        textTransferred = findViewById<View>(R.id.textTransferred) as TextView
        textBalance = findViewById<View>(R.id.textBalance) as TextView
        textDealerCount = findViewById<View>(R.id.textDealerCount) as TextView
        textCredit = findViewById<View>(R.id.textCredit) as TextView
        textDebit = findViewById<View>(R.id.textDebit) as TextView
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
                mContext, listHistory
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
                mContext, listHistory
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
        if (PreferenceManager.getUserType(mContext).equals("7")) {
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
        TODO("Not yet implemented")
    }
}