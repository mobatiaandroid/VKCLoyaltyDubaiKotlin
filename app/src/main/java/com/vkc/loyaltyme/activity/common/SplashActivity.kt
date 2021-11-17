package com.vkc.loyaltyme.activity.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.dealers.DealersActivity
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.UtilityMethods

class SplashActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        context = this
        PreferenceManager.setAgreeTerms(context,true)
        loadSplash()

    }
    private fun loadSplash() {
        val countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (PreferenceManager.getAgreeTerms(context)) {
                    startNextActivity()
                } else {
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            TermsAndConditionsActivity::class.java
                        )
                    )
                    activity.overridePendingTransition(0, 0);
                    finish()
                }
            }
        }
        countDownTimer.start()
    }

    private fun startNextActivity() {
        if (isFinishing)
            return
        if (PreferenceManager.getLoginStatusFlag(context).equals("Y")) {
            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )
            finish()
        } else if (PreferenceManager.getIsVerifiedOTP(context).equals("Y")) {
            if (PreferenceManager.getDealerCount(context) > 0) {
                startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
                finish()
            } else {
                startActivity(
                    Intent(
                        this,
                        DealersActivity::class.java
                    )
                )
                finish()
            }
        } else {
            startActivity(
                Intent(
                    this,
                    SignUpActivity::class.java
                )
            )
            finish()
        }
    }
}

