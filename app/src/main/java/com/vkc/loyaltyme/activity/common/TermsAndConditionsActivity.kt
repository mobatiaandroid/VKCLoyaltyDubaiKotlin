package com.vkc.loyaltyme.activity.common

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.CheckBox
import com.vkc.loyaltyme.utils.CustomToast
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.dealers.DealersActivity
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.manager.PreferenceManager

class TermsAndConditionsActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var context: Activity
    lateinit var checkTerms: CheckBox
    lateinit var buttonNext: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        context = this
        initialiseUI()
    }

    private fun initialiseUI() {
        webView = findViewById(R.id.webTerms)
        checkTerms = findViewById(R.id.checkboxTerms)
        buttonNext = findViewById(R.id.buttonNext)
        buttonNext.setOnClickListener {
            if (PreferenceManager.getAgreeTerms(context)){
                if (PreferenceManager.getLoginStatusFlag(context).equals("Y")){
                    startActivity(Intent(context, HomeActivity::class.java))
                    finish()
                }else if(PreferenceManager.getIsVerifiedOTP(context).equals("Y")){
                    if (PreferenceManager.getDealerCount(context) > 0){
                        startActivity(Intent(context, HomeActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(context, DealersActivity::class.java))
                        finish()
                    }
                }else{
                    startActivity(Intent(context, SignUpActivity::class.java))
                    finish()
                }
            }else{
                CustomToast.customToast(context)
                CustomToast.show(37)
            }
        }
        checkTerms.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                PreferenceManager.setAgreeTerms(context, true)
            } else {
                PreferenceManager.setAgreeTerms(context, false)
            }
        }
        webView.loadUrl("file:///android_asset/terms.html")
    }
}