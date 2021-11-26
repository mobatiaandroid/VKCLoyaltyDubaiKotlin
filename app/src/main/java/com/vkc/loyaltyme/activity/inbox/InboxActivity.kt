package com.vkc.loyaltyme.activity.inbox

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vkc.loyaltyme.utils.CustomToast
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.activity.inbox.adapter.InboxAdapter
import com.vkc.loyaltyme.activity.inbox.model.notification.Data
import com.vkc.loyaltyme.activity.inbox.model.notification.NotificationModel
import com.vkc.loyaltyme.activity.inbox.model.notification.Response
import com.vkc.loyaltyme.api.ApiClient
import com.vkc.loyaltyme.app_controller.AppController
import com.vkc.loyaltyme.manager.HeaderManager
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.ProgressBarDialog
import com.vkc.loyaltyme.utils.UtilityMethods
import retrofit2.Call
import retrofit2.Callback

class InboxActivity : AppCompatActivity() {
    lateinit var context: Activity
    lateinit var headerManager: HeaderManager
    lateinit var header: LinearLayout
    lateinit var imageBack: ImageView
    lateinit var recyclerInboxList: RecyclerView
    lateinit var adapter: InboxAdapter
    lateinit var progressBarDialog: ProgressBarDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        context = this
        initialiseUI()
    }

    private fun initialiseUI() {
        header = findViewById(R.id.header)
        recyclerInboxList = findViewById(R.id.recyclerListInbox)
        headerManager = HeaderManager(this@InboxActivity, resources.getString(R.string.inbox))
        headerManager.getHeader(header, 1)
        imageBack = headerManager.leftButton!!
        progressBarDialog = ProgressBarDialog(context)
        headerManager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        imageBack.setOnClickListener {
            val intent = Intent(context,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        getInbox()
    }

    private fun getInbox() {
        AppController.notificationsList.clear()
        var notificationMainResponse: NotificationModel
        var notificationResponse: Response
        var notificationData: ArrayList<Data>
        var tempModel = Data("","","", "",
            "", "","","")
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            ApiClient.getApiService().getNotificationResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context)
            ).enqueue(object : Callback<NotificationModel> {
                override fun onResponse(
                    call: Call<NotificationModel>,
                    response: retrofit2.Response<NotificationModel>
                ) {
                    progressBarDialog.hide()
                    notificationMainResponse = response.body()!!
                    notificationResponse = notificationMainResponse.response
                    if (notificationResponse.status.equals("Success")){
                        notificationData = notificationResponse.data
                        if (notificationData.size > 0){
                            for (i in notificationData.indices) {
                                tempModel = Data("","","","",
                                    "","","","")
                                tempModel.title = notificationData[i].title
                                tempModel.message = notificationData[i].message
                                tempModel.image = notificationData[i].image
                                tempModel.createdon = notificationData[i].createdon
                                tempModel.from_date = notificationData[i].from_date
                                tempModel.to_date = notificationData[i].to_date
                                tempModel.pdf = notificationData[i].pdf
                                tempModel.notification_type = notificationData[i].notification_type
                                AppController.notificationsList.add(tempModel)
                            }

                        }else{
                            CustomToast.customToast(context)
                            CustomToast.show(50)
                        }
                        Log.e("Notification",AppController.notificationsList.toString())
                        adapter = InboxAdapter(context, AppController.notificationsList)
                        recyclerInboxList.hasFixedSize()
                        recyclerInboxList.layoutManager = LinearLayoutManager(
                            context,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recyclerInboxList.adapter = adapter
                    }else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<NotificationModel>, t: Throwable) {
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
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
    }
}