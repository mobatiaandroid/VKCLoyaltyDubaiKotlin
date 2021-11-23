package com.vkc.loyaltyme.activity.home

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.lzyzsd.circleprogress.ArcProgress
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.vkc.loyaltyme.utils.CustomToast
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.home.model.app_version.AppVersionModel
import com.vkc.loyaltyme.activity.home.model.device_registration.DeviceRegistrationModel
import com.vkc.loyaltyme.activity.home.model.my_points.MyPointsModel
import com.vkc.loyaltyme.activity.home.model.my_points.Response
import com.vkc.loyaltyme.activity.inbox.InboxActivity
import com.vkc.loyaltyme.activity.issue_points.IssuePointsActivity
import com.vkc.loyaltyme.activity.point_history.PointHistoryActivity
import com.vkc.loyaltyme.activity.profile.ProfileActivity
import com.vkc.loyaltyme.api.ApiClient
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.ProgressBarDialog
import com.vkc.loyaltyme.utils.UtilityMethods
import devlight.io.library.ArcProgressStackView
import retrofit2.Call
import retrofit2.Callback
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {
    lateinit var context: Activity
    private lateinit var textPoints: TextView
    lateinit var textVersion: TextView
    lateinit var textNoPoints: TextView
    private lateinit var llProfile: LinearLayout
    lateinit var llPoints: LinearLayout
    lateinit var llDescription: LinearLayout
    lateinit var llInbox: LinearLayout
    private lateinit var buttonIssue: Button
    lateinit var progressStackView: ArcProgressStackView
    lateinit var arcProgress: ArcProgress
    private lateinit var updateAppPromptView: ConstraintLayout
    lateinit var progressBarDialog: ProgressBarDialog
    private val modelCount = 2
    private var myPoints: Int = 0
    private var giftStatus: String = ""
    private val mStartColors = IntArray(modelCount)
    private val mEndColors = IntArray(modelCount)
    private var serverVersion = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        context = this
        Firebase.initialize(context)
        initialiseUI()
        getMyPoints()
        getAppVersion()
    }
    private fun initialiseUI() {
        textPoints = findViewById(R.id.textPoint)
        textNoPoints = findViewById(R.id.textNoPoint)
        textVersion = findViewById(R.id.textVersion)
        llProfile = findViewById(R.id.llProfile)
        llPoints = findViewById(R.id.llPoints)
        llInbox = findViewById(R.id.llInbox)
        llDescription = findViewById(R.id.llDescription)
        updateAppPromptView = findViewById(R.id.updateApp)
        buttonIssue = findViewById(R.id.buttonIssue)
        progressStackView = findViewById(R.id.arcProgressStackView)
        arcProgress = findViewById(R.id.arc_progress)
        progressBarDialog = ProgressBarDialog(context)
        llProfile.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    ProfileActivity::class.java
                )
            )
        }
        llPoints.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    PointHistoryActivity::class.java
                )
            )
        }
        llInbox.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    InboxActivity::class.java
                )
            )
        }
        buttonIssue.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    IssuePointsActivity::class.java
                )
            )
        }
        if (PreferenceManager.getUserType(context).equals("7")){
            buttonIssue.visibility = View.VISIBLE
            progressStackView.visibility = View.GONE
            arcProgress.visibility = View.VISIBLE
            llDescription.visibility = View.GONE
        }else{
            buttonIssue.visibility = View.GONE
            progressStackView.visibility = View.VISIBLE
            arcProgress.visibility = View.GONE
            llDescription.visibility = View.VISIBLE
        }
        val startColors = resources.getStringArray(R.array.devlight)
        val bgColors = resources.getStringArray(R.array.bg)

        // Parse colors
        for (i in 0 until modelCount) {
            mStartColors[i] = Color.parseColor(startColors[i])
            mEndColors[i] = Color.parseColor(bgColors[i])
        }
    }

    private fun getAppVersion() {
        var appVersionMainResponse: AppVersionModel
        var appVersionResponse: com.vkc.loyaltyme.activity.home.model.app_version.Response
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            ApiClient.getApiService().getAppVersionResponse()
                .enqueue(object : Callback<AppVersionModel> {
                    override fun onResponse(
                        call: Call<AppVersionModel>,
                        response: retrofit2.Response<AppVersionModel>
                    ) {
                        progressBarDialog.hide()
                        if (response.body() != null){
                            appVersionMainResponse = response.body()!!
                            appVersionResponse = appVersionMainResponse.response
                            if (appVersionResponse.status.equals("Success")){
                                serverVersion = appVersionResponse.appversion
                                if (serverVersion.equals(getVersion())){
                                    deviceRegister()
                                    updateAppPromptView.visibility = View.GONE
                                }else{
                                    updateAppPromptView.visibility = View.VISIBLE
                                    updateApp(context)
                                }
                            }else{
                                CustomToast.customToast(context)
                                CustomToast.show(0)
                            }
                        }else{
                            CustomToast.customToast(context)
                            CustomToast.show(0)
                        }
                    }

                    override fun onFailure(call: Call<AppVersionModel>, t: Throwable) {
                        progressBarDialog.hide()
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }

                })
        }else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun updateApp(context: Activity) {
        val appPackageName: String = context.packageName
        val builder = AlertDialog.Builder(context)
        builder.setTitle("New Update Available !")
            .setMessage("Please update the app to avail new features") //
            .setPositiveButton("Ok") { _, _ ->
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (exception: ActivityNotFoundException) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun deviceRegister() {
        var deviceRegistrationMainResponse: DeviceRegistrationModel
        var deviceRegistrationResponse: com.vkc.loyaltyme.activity.home.model.device_registration.Response
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result
            PreferenceManager.setToken(context, token)
        })
        Log.e("Token", PreferenceManager.getToken(context))
        if (UtilityMethods.checkInternet(context)) {
            progressBarDialog.show()
            ApiClient.getApiService().getDeviceRegistrationResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context),
                PreferenceManager.getToken(context)
            ).enqueue(object : Callback<DeviceRegistrationModel> {
                override fun onResponse(
                    call: Call<DeviceRegistrationModel>,
                    response: retrofit2.Response<DeviceRegistrationModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() != null){
                        deviceRegistrationMainResponse = response.body()!!
                        deviceRegistrationResponse = deviceRegistrationMainResponse.response
                        if (deviceRegistrationResponse.status.equals("Success")){

                        }else if (deviceRegistrationResponse.status.equals("Empty")){
                            deviceRegister()
                        }else{
                            CustomToast.customToast(context)
                            CustomToast.show(0)
                        }
                    }else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<DeviceRegistrationModel>, t: Throwable) {
                    progressBarDialog.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }
            })
        }else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun getVersion(): String? {
        val packageInfo: PackageInfo = packageManager.getPackageInfo(
            packageName, 0
        )
        return packageInfo.versionName
    }

    private fun getMyPoints() {
        var myPointsMainResponse: MyPointsModel
        var myPointsResponse: Response
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            ApiClient.getApiService().getMyPointsResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context)
            ).enqueue(object : Callback<MyPointsModel> {
                override fun onResponse(
                    call: Call<MyPointsModel>,
                    response: retrofit2.Response<MyPointsModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() != null){
                        myPointsMainResponse = response.body()!!
                        myPointsResponse = myPointsMainResponse.response
                        Log.e("Response",myPointsResponse.toString())
                        if (myPointsResponse.status.equals("Success")){
                            val points = myPointsResponse.loyality_point
                            myPoints = points.toInt()
                            giftStatus = myPointsResponse.gift_status
                            if (myPoints.toString().equals("0")){
                                textNoPoints.visibility = View.VISIBLE
                                textPoints.visibility = View.GONE
                            }else{
                                textNoPoints.visibility = View.GONE
                                textPoints.text = "$myPoints Coupons"
                            }
                            val multipliedValue: Int = myPoints * 100
                            val percentValue = multipliedValue / 1600
                            progressStackView.textColor = Color.parseColor("#000000")
                            progressStackView.drawWidthDimension = 150f
                            if (PreferenceManager.getUserType(context).equals("7")){
                                buttonIssue.visibility = View.VISIBLE
                                progressStackView.visibility = View.GONE
                                arcProgress.visibility = View.VISIBLE
                                llDescription.visibility = View.GONE
                                arcProgress.progress = myPoints
                            }else{
                                buttonIssue.visibility = View.GONE
                                progressStackView.visibility = View.VISIBLE
                                arcProgress.visibility = View.GONE
                                llDescription.visibility = View.VISIBLE
                                val models = ArrayList<ArcProgressStackView.Model>()
                                models.add(
                                    ArcProgressStackView.Model(
                                        "",
                                        (100).toFloat(),
                                        mEndColors[0],
                                        mStartColors[0]
                                    )
                                )
                                models.add(
                                    ArcProgressStackView.Model(
                                        "",
                                        percentValue.toFloat(),
                                        mEndColors[1],
                                        mStartColors[1]
                                    )
                                )
                                progressStackView.models = models
                            }
                        }else{
                            CustomToast.customToast(context)
                            CustomToast.show(0)
                        }
                    }else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<MyPointsModel>, t: Throwable) {
                    progressBarDialog.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        }else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}