package com.vkc.loyaltyme.activity.issue_points

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.github.lzyzsd.circleprogress.ArcProgress
import com.vkc.loyaltyme.utils.CustomToast
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.activity.home.model.my_points.MyPointsModel
import com.vkc.loyaltyme.activity.issue_points.model.submit_points.SubmitPointsResponse
import com.vkc.loyaltyme.activity.issue_points.model.user.Data
import com.vkc.loyaltyme.activity.issue_points.model.user.Response
import com.vkc.loyaltyme.activity.issue_points.model.user.UserModel
import com.vkc.loyaltyme.activity.issue_points.model.user_type.TypeModel
import com.vkc.loyaltyme.api.ApiClient
import com.vkc.loyaltyme.manager.HeaderManager
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.ProgressBarDialog
import com.vkc.loyaltyme.utils.UtilityMethods
import retrofit2.Call
import retrofit2.Callback
import java.util.*

class IssuePointsActivity : AppCompatActivity() {
    lateinit var context: Activity
    lateinit var header: LinearLayout
    lateinit var headerManager: HeaderManager
    lateinit var autoSearch: AutoCompleteTextView
    lateinit var textID: TextView
    lateinit var textName: TextView
    lateinit var textAddress: TextView
    lateinit var textPhone: TextView
    lateinit var editPoints: EditText
    lateinit var buttonIssue: ImageView
    lateinit var imageBack: ImageView
    lateinit var llData: LinearLayout
    lateinit var arcProgress: ArcProgress
    lateinit var progressBarDialog: ProgressBarDialog
    var sampleList = arrayListOf<String>("Dealer","Retailer","Sub-Dealer")
    var listUsers: ArrayList<com.vkc.loyaltyme.activity.issue_points.model.user_type.Data>? = null
    var selectedId: String? = null
    var myPoint = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue_points)
        context = this
        initialiseUI()
        getMyPoints()
    }
    private fun initialiseUI() {
        header = findViewById(R.id.header)
        autoSearch = findViewById(R.id.autoSearch)
        textID = findViewById(R.id.textViewId)
        textName = findViewById(R.id.textViewName)
        textAddress = findViewById(R.id.textViewAddress)
        textPhone = findViewById(R.id.textViewPhone)
        editPoints = findViewById(R.id.editPoints)
        buttonIssue = findViewById(R.id.buttonIssue)
        llData = findViewById(R.id.llData)
        arcProgress = findViewById(R.id.arc_progress)
        progressBarDialog = ProgressBarDialog(context)
        headerManager =
            HeaderManager(this@IssuePointsActivity,
                resources.getString(R.string.issue_point))
        headerManager.getHeader(header, 1)
        imageBack = headerManager.leftButton!!
        headerManager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        llData.visibility = View.GONE
        arcProgress.suffixText = ""
        arcProgress.strokeWidth = 15f
        arcProgress.bottomTextSize = 50f
        arcProgress.max = 10000000
        autoSearch.setText(sampleList[0])
        /***getColor deprecated***/
        arcProgress.textColor = getColor(R.color.white)
        arcProgress.setBackgroundColor(getColor(R.color.transparent))
        arcProgress.unfinishedStrokeColor = getColor(R.color.white)
        imageBack.setOnClickListener {
            val intent = Intent(context,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonIssue.setOnClickListener {
            if (autoSearch.text.toString().trim { it <= ' ' } == "") {
                CustomToast.customToast(context)
                CustomToast.show(14)
            } else if (editPoints.text.toString().trim { it <= ' ' } == "") {
                CustomToast.customToast(context)
                CustomToast.show(17)
            } else if (editPoints.text.toString().trim { it <= ' ' }.toInt() > myPoint) {
                CustomToast.customToast(context)
                CustomToast.show(16)
            } else {
                submitPoints()
            }
        }

        autoSearch.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            val selectedData: String = autoSearch.text.toString()
            for (i in listUsers!!.indices) {
                if (listUsers!![i].name.equals(selectedData)) {
                    selectedId = listUsers!![i].id
                    getUserData()
                    break
                } else {
                    selectedId = ""
                }
            }
        }


        autoSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (selectedId!!.isNotEmpty()) {
                        llData.visibility = View.VISIBLE
                    }
                } else {
                    selectedId = ""
                    llData.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        autoSearch.setOnTouchListener { _, _ ->
            autoSearch.showDropDown()
            false
        }

        autoSearch.setOnClickListener {
            autoSearch.showDropDown()
        }
    }


    private fun getMyPoints() {
        var myPointsMainResponse: MyPointsModel
        var myPointsResponse: com.vkc.loyaltyme.activity.home.model.my_points.Response
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
                        if (myPointsResponse.status.equals("Success")){
                            val points = myPointsResponse.loyality_point
                            myPoint = points.toInt()
                            arcProgress.progress = myPoint
                            getUsers()
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

    private fun getUsers() {
        var typeMainResponse: TypeModel
        var typeResponse: com.vkc.loyaltyme.activity.issue_points.model.user_type.Response
        var typeData: ArrayList<com.vkc.loyaltyme.activity.issue_points.model.user_type.Data>
        var tempModel: com.vkc.loyaltyme.activity.issue_points.model.user_type.Data
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            ApiClient.getApiService().getRetailersResponse(
                PreferenceManager.getUserType(context)
            ).enqueue(object : Callback<TypeModel>{
                override fun onResponse(
                    call: Call<TypeModel>,
                    response: retrofit2.Response<TypeModel>
                ) {
                    progressBarDialog.hide()
                    if (response != null){
                        typeMainResponse = response.body()!!
                        typeResponse = typeMainResponse.response
                        if (typeResponse.status.equals("Success")){
                            typeData = typeResponse.data
                            if (typeData.size > 0){
                                tempModel =
                                    com.vkc.loyaltyme.activity.issue_points.model.user_type.Data("","")
                                for (i in typeData.indices){
                                    tempModel.id = typeData[i].id
                                    tempModel.name = typeData[i].name
                                    listUsers!!.add(tempModel)
                                }
                                val listUser = ArrayList<String>()
                                for (i in listUsers!!.indices) {
                                    listUser.add(
                                        listUsers!![i].name
                                    )
                                }

                                val adapter = ArrayAdapter(
                                    context,
                                    android.R.layout.simple_list_item_1,
                                    listUser
                                )
                                autoSearch.threshold = 1
                                autoSearch.setAdapter<ArrayAdapter<String>>(adapter)
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

                override fun onFailure(call: Call<TypeModel>, t: Throwable) {
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


    private fun submitPoints() {
        var submitPointsResponse: SubmitPointsResponse
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            ApiClient.getApiService().getSubmitPointsResponse(
                PreferenceManager.getCustomerID(context),
                selectedId.toString(),
                "5",
                editPoints.text.toString(),
                "7"
            ).enqueue(object : Callback<SubmitPointsResponse>{
                override fun onResponse(
                    call: Call<SubmitPointsResponse>,
                    response: retrofit2.Response<SubmitPointsResponse>
                ) {
                    progressBarDialog.hide()
                    submitPointsResponse = response.body()!!
                    if (submitPointsResponse.response == 1){
                        CustomToast.customToast(context)
                        CustomToast.show(18)
                        autoSearch.setText("")
                        editPoints.setText("")
                        getMyPoints()
                    }else if (submitPointsResponse.response == 5){
                        CustomToast.customToast(context)
                        CustomToast.show(61)
                    }else{
                        CustomToast.customToast(context)
                        CustomToast.show(13)
                    }
                }

                override fun onFailure(call: Call<SubmitPointsResponse>, t: Throwable) {
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

    private fun getUserData() {
        var userMainResponse: UserModel
        var userResponse: Response
        var userData: Data
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            ApiClient.getApiService().getUserResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context)
            ).enqueue(object : Callback<UserModel>{
                override fun onResponse(
                    call: Call<UserModel>,
                    response: retrofit2.Response<UserModel>
                ) {
                    progressBarDialog.hide()
                    if (response.body() != null){
                        userMainResponse = response.body()!!
                        userResponse = userMainResponse.response
                        if (userResponse.status.equals("Success")){
                            userData = userResponse.data
                            val custId: String = userData.customer_id
                            val address: String = userData.address
                            val name: String = userData.name
                            val phone: String = userData.phone
                            textID.text = ": $custId"
                            textName.text = ": $name"
                            textAddress.text = ": $address"
                            textPhone.text = ": $phone"
                            llData.visibility = View.VISIBLE
                        }else{
                            CustomToast.customToast(context)
                            CustomToast.show(0)
                        }
                    }else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
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