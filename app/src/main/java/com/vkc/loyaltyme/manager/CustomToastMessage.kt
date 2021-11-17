package com.vkc.loyaltyapp.manager

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vkc.loyaltyme.R

class CustomToastMessage(var mActivity: AppCompatActivity, var message: String) {
    var mTextView: TextView? = null
    var mToast: Toast? = null
    fun init() {
        val inflater = mActivity.layoutInflater
        val layouttoast: View = inflater.inflate(R.layout.custom_toast, null)
        mTextView = layouttoast.findViewById<View>(R.id.texttoast) as TextView
        mToast = Toast(mActivity)
        mToast!!.setView(layouttoast)
        mTextView!!.text = message
        mToast!!.duration = Toast.LENGTH_SHORT
        mToast!!.show()
    }

    init {
        init()
    }
}