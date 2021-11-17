package com.vkc.loyaltyme.activity.inbox

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.app_controller.AppController
import com.vkc.loyaltyme.manager.HeaderManager

class InboxDetailsActivity : AppCompatActivity() {
    lateinit var context: Activity
    lateinit var headerManager: HeaderManager
    lateinit var header: LinearLayout
    lateinit var imageBack: ImageView
    lateinit var imageNotification: ImageView
    lateinit var textTitle: TextView
    lateinit var textPinch:TextView
    lateinit var webMessage: WebView
    lateinit var webImage: WebView
    var position = 0
    var title: String? = null
    var message: String? = null
    var createdOn: String? = null
    var image: String? = null
    var dateFrom: String? = null
    var dateTo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox_details)
        context = this
        val intent = intent
        position =
            intent.extras!!.getInt("position")
        initialiseUI()

    }

    private fun initialiseUI() {
        header = findViewById(R.id.header)
        headerManager =
            HeaderManager(this@InboxDetailsActivity, resources.getString(R.string.inbox))
        headerManager.getHeader(header, 1)
        imageBack = headerManager.leftButton!!
        headerManager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        textTitle = findViewById<View>(R.id.textTitle) as TextView
        imageNotification = findViewById<View>(R.id.imageNotification) as ImageView
        webMessage = findViewById<View>(R.id.webMessage) as WebView
        message = AppController.notificationsList[position].message
        image = AppController.notificationsList[position].image
        title = AppController.notificationsList[position].title
        if (image!!.trim().isNotEmpty()){
            webImage.visibility = View.VISIBLE
            textPinch.visibility = View.VISIBLE
            val summary =
                        "<html>" +
                        "<body style=\"color:white;\">" +
                        "<center>" +
                        "<img  src='$image'width='100%', height='auto'\">" +
                        "</center>" +
                        "</body>" +
                        "</html>"
            webImage.setBackgroundColor(Color.TRANSPARENT)
            webImage.loadData(summary, "text/html", null)
        } else {
            webImage.visibility = View.GONE
            textPinch.visibility = View.GONE
        }
        imageBack.setOnClickListener {
            finish()
        }
        textTitle.text = title
        val summary = "<html><body style=\"color:white;\">$message</body></html>"
        webImage.setBackgroundColor(Color.TRANSPARENT)
        webImage.loadData(summary, "text/html", null)

    }
}