package com.vkc.loyaltyme.activity.common

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings.Secure.*
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.common.model.district.DistrictResponseModel
import com.vkc.loyaltyme.activity.common.model.new_register.NewRegisterModel
import com.vkc.loyaltyme.activity.common.model.register.RegisterModel
import com.vkc.loyaltyme.activity.common.model.resend_otp.ResendOTPModel
import com.vkc.loyaltyme.activity.common.model.state.State
import com.vkc.loyaltyme.activity.common.model.state.StateResponseModel
import com.vkc.loyaltyme.activity.common.model.user_details.Data
import com.vkc.loyaltyme.activity.common.model.user_details.UserDetailsResponseModel
import com.vkc.loyaltyme.activity.common.model.verify_otp.VerifyOTPModel
import com.vkc.loyaltyme.activity.dealers.DealersActivity
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.api.ApiClient
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.CustomToast
import com.vkc.loyaltyme.utils.ProgressBarDialog
import com.vkc.loyaltyme.utils.UtilityMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class SignUpActivity : AppCompatActivity() {
    lateinit var context: Activity
    lateinit var buttonRegister: Button
    lateinit var editMobile: EditText
    lateinit var editOwner: EditText
    lateinit var editPlace: EditText
    lateinit var editPin: EditText
    lateinit var editShop: EditText
    lateinit var editCustomer: EditText
    lateinit var editDoor: EditText
    lateinit var editAddress: EditText
    lateinit var editLandmark: EditText
    lateinit var llAddress: LinearLayout
    lateinit var llCustomerID: ConstraintLayout
    lateinit var llUserType: LinearLayout
    lateinit var imageSearch: ImageView
    lateinit var imageGetData: ImageView
    lateinit var mobileNo: String
    lateinit var selectedState: String
    lateinit var selectedDistrict: String
    lateinit var stateID: String
    lateinit var district: String
    lateinit var spinnerState: Spinner
    lateinit var spinnerUserType: Spinner
    lateinit var spinnerDistrict: Spinner
    lateinit var listState: ArrayList<String>
    lateinit var listDistrict: ArrayList<String>
    lateinit var listUserType: ArrayList<String>
    lateinit var stateList: ArrayList<State>
    lateinit var districtList: ArrayList<com.vkc.loyaltyme.activity.common.model.district.Response>
    lateinit var progressBar: ProgressBarDialog
    var otpValue = ""
    var androidID = ""
    var isNewReg = false
    var userType = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        context = this
        initUI()
    }

    private fun initUI() {
        listState = ArrayList()
        listDistrict = ArrayList()
        stateList = ArrayList()
        districtList = ArrayList()
        district = ""
        progressBar = ProgressBarDialog(context)
        listUserType = ArrayList()
        buttonRegister = findViewById<View>(R.id.buttonRegister) as Button
        editMobile = findViewById<View>(R.id.editMobile) as EditText
        editOwner = findViewById<View>(R.id.editOwner) as EditText
        editShop = findViewById<View>(R.id.editShop) as EditText
        editDoor = findViewById<View>(R.id.editDoor) as EditText
        editAddress = findViewById<View>(R.id.editAddress) as EditText
        editLandmark = findViewById<View>(R.id.editLandMark) as EditText
        spinnerState = findViewById<View>(R.id.spinnerState) as Spinner
        spinnerUserType = findViewById<View>(R.id.spinnerUserType) as Spinner
        spinnerDistrict = findViewById<View>(R.id.spinnerDistrict) as Spinner
        editPlace = findViewById<View>(R.id.editPlace) as EditText
        editPin = findViewById<View>(R.id.editPin) as EditText
        editCustomer = findViewById<View>(R.id.editCustId) as EditText
        imageSearch = findViewById<View>(R.id.imageSearchID) as ImageView
        imageGetData = findViewById<View>(R.id.imageGetData) as ImageView
        llCustomerID = findViewById<View>(R.id.llCustId) as ConstraintLayout
        llAddress = findViewById<View>(R.id.llAddress) as LinearLayout
        llUserType = findViewById<View>(R.id.llUserType) as LinearLayout
        llCustomerID.visibility = View.GONE
        llAddress.visibility = View.GONE
        llUserType.visibility = View.GONE
        editOwner.isEnabled = false
        editShop.isEnabled = false
        editPlace.isEnabled = false
        editPin.isEnabled = false
        editOwner.filters = arrayOf<InputFilter>(AllCaps())
        editPlace.filters = arrayOf<InputFilter>(AllCaps())
        editShop.filters = arrayOf<InputFilter>(AllCaps())
        editDoor.filters = arrayOf<InputFilter>(AllCaps())
        editAddress.filters = arrayOf<InputFilter>(AllCaps())
        editLandmark.filters = arrayOf<InputFilter>(AllCaps())
        androidID = getString(contentResolver, ANDROID_ID)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result
            Log.e("token", token.toString())
        })

        imageSearch.setOnClickListener {
            if (editCustomer.text.toString().trim { it <= ' '} == "")  {
                editCustomer.requestFocus()
                editCustomer.error = "Mandatory Field"
            }else{
                getData()
            }
        }
        imageGetData.setOnClickListener {
            if (editMobile.text.toString().trim { it <= ' '} == "")  {
                editMobile.requestFocus()
                editMobile.error = "Mandatory Field"
            }else{
                getData()
            }
        }
        buttonRegister.setOnClickListener {
            if (editMobile.text.toString().trim { it <= ' ' } == "") {
                editMobile.requestFocus()
                editMobile.error = "Mandatory Field"
            } else if (editOwner.text.toString().trim { it <= ' ' } == "") {
                editOwner.requestFocus()
                editOwner.error = "Mandatory Field"
            } else if (editShop.text.toString().trim { it <= ' ' } == "") {
                editShop.requestFocus()
                editShop.error = "Mandatory Field"
            } else if (userType == "" && isNewReg) {
                CustomToast.customToast(context)
                CustomToast.show(57)
            } else if (editDoor.text.toString().trim { it <= ' ' } == "" && isNewReg) {
                editDoor.requestFocus()
                editDoor.error = "Mandatory Field"
            } else if (editAddress.text.toString().trim { it <= ' ' } == "" && isNewReg) {
                editAddress.requestFocus()
                editAddress.error = "Mandatory Field"
            } else if (selectedState == "") {
                CustomToast.customToast(context)
                CustomToast.show(32)
            } else if (selectedDistrict == "") {
                CustomToast.customToast(context)
                CustomToast.show(33)
            } else if (editPlace.text.toString().trim { it <= ' ' } == "") {
                editPlace.requestFocus()
                editPlace.error = "Mandatory Field"
            } else if (editPin.text.toString().trim { it <= ' ' } == "") {
                editPin.requestFocus()
                editPin.error = "Mandatory Field"
            } else {
                if (isNewReg) {
                    newRegisterAPI()
                } else {
                    registerAPI()
                }
            }
        }
        editCustomer.setText("")
        userType = ""
        listUserType.clear()
        editMobile.setText("")
        listUserType.add("User Type")
        listUserType.add("Retailer")
        listUserType.add("Subdealer")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@SignUpActivity,
            android.R.layout.simple_spinner_item,
            listUserType
        )
        spinnerUserType.adapter = adapter
        getState()


        editCustomer.setOnKeyListener { v, keyCode, event ->
            if (keyCode.equals(KeyEvent.KEYCODE_DPAD_CENTER)
                || keyCode.equals(KeyEvent.KEYCODE_ENTER)
            ) {
                getData()
            }
            false
        }
        spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    (parent.getChildAt(0) as TextView).textSize = 14f
                    (parent.getChildAt(0) as TextView).isAllCaps = true
                    stateID = stateList[position - 1].state
                    selectedState = stateList[position - 1].state_name
                    getDistrict(stateID)
                } else {
                    stateID = ""
                    selectedState = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedState = ""
                stateID = ""
            }
        }
        spinnerUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    (parent.getChildAt(0) as TextView).textSize = 14f
                    (parent.getChildAt(0) as TextView).isAllCaps = true
                    if (listUserType[position] == "Retailer") {
                        userType = "5"
                    } else if (listUserType[position] == "Subdealer") {
                        userType = "7"
                    }
                } else {
                    userType = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                userType = ""
            }
        }
        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    (parent.getChildAt(0) as TextView).isAllCaps = true
                    (parent.getChildAt(0) as TextView).textSize = 14f
                    selectedDistrict = districtList[position - 1].district
                } else {
                    selectedDistrict = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        editMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun getState() {
        stateList.clear()
        listState.clear()
        var stateResponse: StateResponseModel
        var i = 0
        if (UtilityMethods.checkInternet(context)) {
            progressBar.show()
            ApiClient.getApiService().getStateResponse()
                .enqueue(object : Callback<StateResponseModel> {
                    override fun onResponse(
                        call: Call<StateResponseModel>,
                        response: Response<StateResponseModel>
                    ) {
                        progressBar.hide()
                        stateResponse = response.body()!!
                        while (i < stateResponse.states.size) {
                            stateList.add(stateResponse.states[i])
                            i++
                        }
                        i = 0
                        listState.add("Select State")
                        while (i < stateList.size) {
                            listState.add(stateList[i].state_name)
                            i++
                        }
                        val adapter = ArrayAdapter(
                            this@SignUpActivity,
                            android.R.layout.simple_spinner_item,
                            listState
                        )
                        spinnerState.adapter = adapter
                    }

                    override fun onFailure(call: Call<StateResponseModel>, t: Throwable) {
                        progressBar.hide()
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }

                })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun getDistrict(stateID: String) {
        listDistrict.clear()
        districtList.clear()
        var districtResponse: DistrictResponseModel
        var i = 0
        if (UtilityMethods.checkInternet(context)) {
            progressBar.show()
            ApiClient.getApiService().getDistrictResponse(stateID)
                .enqueue(object : Callback<DistrictResponseModel> {
                    override fun onResponse(
                        call: Call<DistrictResponseModel>,
                        response: Response<DistrictResponseModel>
                    ) {
                        progressBar.hide()
                        districtResponse = response.body()!!
                        while (i < districtResponse.response.size) {
                            districtList.add(districtResponse.response[i])
                            i++
                        }
                        i = 0
                        listDistrict.add("Select District")
                        while (i < districtList.size) {
                            listDistrict.add(districtList[i].district)
                            i++
                        }
                        val adapter = ArrayAdapter(
                            this@SignUpActivity,
                            android.R.layout.simple_spinner_item,
                            listDistrict
                        )
                        spinnerDistrict.adapter = adapter
                        for (j in districtList.indices) {
                            if (districtList[j].district.uppercase()
                                    .equals(district.uppercase())
                            ) {
                                spinnerDistrict.setSelection(j + 1)
                            }
                        }
                    }

                    override fun onFailure(call: Call<DistrictResponseModel>, t: Throwable) {
                        progressBar.hide()
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }

                })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun registerAPI() {
        var registerMainResponse: RegisterModel
        var registerResponse: com.vkc.loyaltyme.activity.common.model.register.Response
        if (UtilityMethods.checkInternet(context)) {
            progressBar.show()
            ApiClient.getApiService().getRegisterResponse(
                editMobile.text.toString().trim(),
                PreferenceManager.getUserType(context),
                PreferenceManager.getCustomerID(context),
                editOwner.text.toString().trim(),
                editPlace.text.toString().trim()
            ).enqueue(object : Callback<RegisterModel> {
                override fun onResponse(
                    call: Call<RegisterModel>,
                    response: Response<RegisterModel>
                ) {
                    progressBar.hide()
                    if (response.body() != null) {
                        registerMainResponse = response.body()!!
                        registerResponse = registerMainResponse.response
                        if (registerResponse.status.equals("Success")) {
                            otpAlert()
                        } else {
                            CustomToast.customToast(context)
                            CustomToast.show(0)
                        }
                    } else {
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                    progressBar.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun newRegisterAPI() {
        var newRegisterMainResponse: NewRegisterModel
        var newRegisterResponse: com.vkc.loyaltyme.activity.common.model.new_register.Response
        if (UtilityMethods.checkInternet(context)) {
            progressBar.show()
            ApiClient.getApiService().getNewRegisterResponse(
                editCustomer.text.toString(),
                editShop.text.toString(),
                selectedState,
                selectedDistrict,
                editPlace.text.toString(),
                editPin.text.toString(),
                editOwner.text.toString(),
                editMobile.text.toString(),
                editDoor.text.toString(),
                editAddress.text.toString(),
                editLandmark.text.toString(),
                userType
            ).enqueue(object : Callback<NewRegisterModel> {
                override fun onResponse(
                    call: Call<NewRegisterModel>,
                    response: Response<NewRegisterModel>
                ) {
                    progressBar.hide()
                    if (response.body() != null) {
                        newRegisterMainResponse = response.body()!!
                        newRegisterResponse = newRegisterMainResponse.response
                        if (newRegisterResponse.status.equals("Success")) {
                            showAlertDialog(
                                context, "4",
                                "Registration request submitted successfully. Please login to loyalty app after the confirmation from VKC Group."
                            )
                        } else if (newRegisterResponse.status.equals("Exists")) {
                            showAlertDialog(
                                context,
                                "5",
                                "Registration request already submitted. Please contact VKC Group."
                            )
                        } else {
                            CustomToast.customToast(context)
                            CustomToast.show(0)
                        }

                    } else {
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<NewRegisterModel>, t: Throwable) {
                    progressBar.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    /**Get Data for  User**/
    private fun getData() {
        var userDetailsMainResponse: UserDetailsResponseModel
        var userDetailsResponse: com.vkc.loyaltyme.activity.common.model.user_details.Response
        var userDetails: Data
        if (UtilityMethods.checkInternet(context)) {
            progressBar.show()
            ApiClient.getApiService().getUserDetailsResponse(
                editMobile.text.toString().trim(),
                editCustomer.text.toString().trim(),
                androidID
            ).enqueue(object : Callback<UserDetailsResponseModel> {
                override fun onResponse(
                    call: Call<UserDetailsResponseModel>,
                    response: Response<UserDetailsResponseModel>
                ) {
                    progressBar.hide()
                    if (response.body() != null) {
                        userDetailsMainResponse = response.body()!!
                        userDetailsResponse = userDetailsMainResponse.response
                        if (userDetailsResponse.status.equals("Success")) {
                            userDetails = userDetailsResponse.data
                            val owner: String = userDetails.contact_person
                            val shop_name: String = userDetails.name
                            district = userDetails.district
                            val state: String = userDetails.state_name
                            val pin: String = userDetails.pincode
                            val role: String = userDetails.role
                            val customerID: String = userDetails.cust_id
                            val city: String = userDetails.city
                            val stateCode: String = userDetails.state
                            /**Set State from get Data**/
                            for (i in stateList.indices) {
                                if (stateList[i].state.equals(stateCode)) {
                                    spinnerState.setSelection(i + 1)
                                }
                            }

                            /**Set District from get Data**/
                            for (i in districtList.indices) {
                                if (districtList[i].district.equals(district)) {
                                    spinnerDistrict.setSelection(i + 1)
                                }
                            }

                            val isLoggedIn = userDetails.is_logged_in
                            mobileNo = userDetails.phone
                            /**Set Phone No**/
                            PreferenceManager.setMobileNo(context, mobileNo)
                            if (isLoggedIn.equals("0")) {
                                val dealerCount = userDetails.dealers_count
                                if (dealerCount > 0) {
                                    PreferenceManager.setDealerCount(context, dealerCount)
                                    CustomToast.customToast(context)
                                    CustomToast.show(35)
                                } else {
                                    PreferenceManager.setDealerCount(context, 0)
                                }
                                PreferenceManager.setCustomerID(context, customerID)
                                PreferenceManager.setUserType(context, role)
                                editOwner.setText(owner)
                                editShop.setText(shop_name)
                                editPlace.setText(city)
                                editPin.setText(pin)

                                editOwner.isEnabled = false
                                editShop.isEnabled = false
                                spinnerDistrict.isEnabled = false
                                spinnerState.isEnabled = false
                                editPlace.isEnabled = false
                                editPin.isEnabled = false
                                llCustomerID.visibility = View.GONE
                                llAddress.visibility = View.GONE
                                llUserType.visibility = View.GONE
                                isNewReg = false
                            } else {
                                CustomToast.customToast(context)
                                CustomToast.show(29)
                            }
                        } else if (userDetailsResponse.status.equals("Empty")) {
                            isNewReg = true
                            editOwner.isEnabled = true
                            editShop.isEnabled = true
                            spinnerState.isEnabled = true
                            spinnerDistrict.isEnabled = true
                            editPlace.isEnabled = true
                            editPin.isEnabled = true
                            if (editCustomer.text.isNotEmpty()) {
                                showAlertDialog(
                                    context,
                                    "0",
                                    "You are not a registered user proceed with new registration"
                                )
                            } else {
                                showAlertDialog(
                                    context,
                                    "1",
                                    "You are not registered with this mobile no, kindly search with CUST ID.If you don't know CUST ID please fill up the data for new registration"
                                )
                            }
                            for (i in listState.indices) {
                                if (listState[i] == "United Arab Emirates") {
                                    spinnerState.setSelection(i)
                                    spinnerState.isEnabled = false
                                    break
                                }
                            }
                            llCustomerID.visibility = View.VISIBLE
                            llAddress.visibility = View.VISIBLE
                            llUserType.visibility = View.VISIBLE
                        }
                    } else {
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<UserDetailsResponseModel>, t: Throwable) {
                    progressBar.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun showAlertDialog(context: Activity, type: String, message: String) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_alert)
        val messageText: TextView = dialog.findViewById<View>(R.id.message) as TextView
        val buttonCancel: Button = dialog.findViewById<View>(R.id.buttonCancel) as Button
        val okButton: Button = dialog.findViewById<View>(R.id.buttonOk) as Button
        messageText.text = message
        if (type.equals("1")) {
            editMobile.clearFocus()
            editCustomer.requestFocus()
        }
        buttonCancel.setOnClickListener {
            dialog.dismiss()
            if (type == "1") {
                editCustomer.setText("")
            } else if (type == "2") {
                editCustomer.setText("")
                startActivity(Intent(context, SignUpActivity::class.java))
            } else if (type == "3") {
                startActivity(Intent(context, DealersActivity::class.java))
            } else if (type == "4") {
            } else if (type == "5") {
            } else {
                llUserType.visibility = View.VISIBLE
            }
        }
        okButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun otpAlert() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_otp_alert)
        val editOTP1 = dialog.findViewById<View>(R.id.editOtp1) as EditText
        val editOTP2 = dialog.findViewById<View>(R.id.editOtp2) as EditText
        val editOTP3 = dialog.findViewById<View>(R.id.editOtp3) as EditText
        val editOTP4 = dialog.findViewById<View>(R.id.editOtp4) as EditText
        val textResend = dialog.findViewById<View>(R.id.textResend) as TextView
        val textCancel = dialog.findViewById<View>(R.id.textCancel) as TextView
        val textOTP = dialog.findViewById<View>(R.id.textOtp) as TextView
        val buttonCancel = dialog.findViewById<View>(R.id.buttonCancel) as Button
        val mob = mobileNo.substring(mobileNo.length - 4, mobileNo.length)
        textOTP.text = getString(R.string.otp_sent,mob)
        editOTP1.isCursorVisible = false
        textCancel.setOnClickListener {
            editCustomer.setText("")
            llCustomerID.visibility = View.GONE
            dialog.dismiss()
            val intent = Intent(context, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        textResend.setOnClickListener {
            resendOTP()
            editOTP1.setText("")
            editOTP2.setText("")
            editOTP3.setText("")
            editOTP4.setText("")
        }
        editOTP1.addTextChangedListener(GenericTextWatcher(editOTP1, editOTP2))
        editOTP2.addTextChangedListener(GenericTextWatcher(editOTP2, editOTP3))
        editOTP3.addTextChangedListener(GenericTextWatcher(editOTP3, editOTP4))
        editOTP4.addTextChangedListener(GenericTextWatcher(editOTP4, null))

        editOTP1.setOnKeyListener(GenericKeyEvent(editOTP1, null))
        editOTP2.setOnKeyListener(GenericKeyEvent(editOTP2, editOTP1))
        editOTP3.setOnKeyListener(GenericKeyEvent(editOTP3, editOTP2))
        editOTP4.setOnKeyListener(GenericKeyEvent(editOTP4, editOTP3))

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        editOTP4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 1){
                    if (editOTP1.text.isNotEmpty()
                        && editOTP2.text.isNotEmpty()
                        && editOTP3.text.isNotEmpty()
                        && editOTP4.text.isNotEmpty()){
                        otpValue = editOTP1.text.toString().trim() + editOTP2.text.toString().trim() + editOTP3.text.toString().trim() + editOTP4.text.toString().trim()
                        verifyOTP(otpValue, editMobile.text.toString().trim())
                    }
                }
            }
        })
        dialog.show()
    }

    private fun verifyOTP(otpValue: String, mobileNo: String) {
        var verifyOTPMainResponse: VerifyOTPModel
        var verifyOTPResponse: com.vkc.loyaltyme.activity.common.model.verify_otp.Response
        if (UtilityMethods.checkInternet(context)) {
            progressBar.show()
            ApiClient.getApiService().getVerifyOTPResponse(
                otpValue,
                PreferenceManager.getUserType(context),
                PreferenceManager.getCustomerID(context),
                mobileNo,
                "0"
            ).enqueue(object : Callback<VerifyOTPModel> {
                override fun onResponse(
                    call: Call<VerifyOTPModel>,
                    response: Response<VerifyOTPModel>
                ) {
                    progressBar.hide()
                    if (response.body() != null){
                        verifyOTPMainResponse = response.body()!!
                        verifyOTPResponse = verifyOTPMainResponse.response
                        if (verifyOTPResponse.status.equals("Success")){
                            PreferenceManager.setIsVerifiedOTP(context,"Y")
                            CustomToast.customToast(context)
                            CustomToast.show(8)

                            if (PreferenceManager.getLoginStatusFlag(context).equals("Y")){
                                startActivity(
                                    Intent(
                                        this@SignUpActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            } else{
                                if (editCustomer.text.toString().trim().equals("")){
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            DealersActivity::class.java
                                        )
                                    )
                                    finish()
                                } else{
                                    updateDialog()
                                }
                            }
                        } else{
                            val attempts = verifyOTPResponse.otp_attempt
                            if (attempts.equals("3")){
                                showAlertDialog(context, "2", "Your OTP attempts exceeded. Kindly fill up the data for new registration")
                                PreferenceManager.setIsVerifiedOTP(context,"N")
                            } else{
                                CustomToast.customToast(context)
                                CustomToast.show(9)
                            }
                        }
                    } else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<VerifyOTPModel>, t: Throwable) {
                    progressBar.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun updateDialog() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_update)
        val textYes = dialog.findViewById<View>(R.id.textYes)
        val textNo = dialog.findViewById<View>(R.id.textNo)
        textYes.setOnClickListener {
            dialog.dismiss()
            updatePhone(otpValue, editMobile.text.toString().trim())
        }
        textNo.setOnClickListener {
            dialog.dismiss()
            startActivity(
                Intent(
                    this@SignUpActivity,
                    DealersActivity::class.java
                )
            )
            finish()
        }
        dialog.show()
    }

    private fun updatePhone(otpValue: String, mobileNo: String) {
        var updatePhoneMainResponse: VerifyOTPModel
        var updatePhoneResponse: com.vkc.loyaltyme.activity.common.model.verify_otp.Response
        if (UtilityMethods.checkInternet(context)) {
            progressBar.show()
            ApiClient.getApiService().getVerifyOTPResponse(
                otpValue,
                PreferenceManager.getUserType(context),
                PreferenceManager.getCustomerID(context),
                mobileNo,
                "1"
            ).enqueue(object : Callback<VerifyOTPModel> {
                override fun onResponse(
                    call: Call<VerifyOTPModel>,
                    response: Response<VerifyOTPModel>
                ) {
                    progressBar.hide()
                    if (response.body() != null){
                        updatePhoneMainResponse = response.body()!!
                        updatePhoneResponse = updatePhoneMainResponse.response
                        if (updatePhoneResponse.status.equals("Success")){
                            showAlertDialog(context,"3","Mobile number updated successfully. Please login using new mobile number")
                            startActivity(
                                Intent(
                                    this@SignUpActivity,
                                    DealersActivity::class.java
                                )
                            )
                            finish()
                        } else{
                            PreferenceManager.setIsVerifiedOTP(context,"N")
                            CustomToast.customToast(context)
                            CustomToast.show(9)
                        }
                    } else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<VerifyOTPModel>, t: Throwable) {
                    progressBar.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }


    private fun resendOTP() {
        var resendOTPMainResponse: ResendOTPModel
        var resendOTPResponse: com.vkc.loyaltyme.activity.common.model.resend_otp.Response
        if (UtilityMethods.checkInternet(context)){
            progressBar.show()
            ApiClient.getApiService().getResendOTPResponse(
                PreferenceManager.getUserType(context),
                PreferenceManager.getCustomerID(context)
            ).enqueue(object : Callback<ResendOTPModel> {
                override fun onResponse(
                    call: Call<ResendOTPModel>,
                    response: Response<ResendOTPModel>
                ) {
                    progressBar.hide()
                    if (response.body() != null){
                        resendOTPMainResponse = response.body()!!
                        resendOTPResponse = resendOTPMainResponse.response
                        if (resendOTPResponse.status.equals("Success")){
                            CustomToast.customToast(context)
                            CustomToast.show(34)
                        } else{
                            CustomToast.customToast(context)
                            CustomToast.show(0)
                        }
                    } else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<ResendOTPModel>, t: Throwable) {
                    progressBar.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })

        } else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    class GenericKeyEvent(private val currentView: EditText, private val previousView: EditText?) :
        View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.editOtp1 && currentView.text.isEmpty()) {
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }

    }

    class GenericTextWatcher(private val currentView: View, private val nextView: View?) :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            when (currentView.id) {
                R.id.editOtp1 -> {
                    if (text.length == 1) {
                        nextView!!.requestFocus()
                        currentView.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    } else if (text.isEmpty()){
                        currentView.setBackgroundResource(R.drawable.rounded_rect_line)
                    }
                }
                R.id.editOtp2 -> {
                    if (text.length == 1) {
                        nextView!!.requestFocus()
                        currentView.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    } else if (text.isEmpty()){
                        currentView.setBackgroundResource(R.drawable.rounded_rect_line)
                    }
                }
                R.id.editOtp3 -> {
                    if (text.length == 1) {
                        nextView!!.requestFocus()
                        currentView.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    } else if (text.isEmpty()){
                        currentView.setBackgroundResource(R.drawable.rounded_rect_line)
                    }
                }
                R.id.editOtp4 -> {
                    if (text.length == 1) {
                        currentView.setBackgroundResource(R.drawable.rounded_rect_full_white)
                    } else if (text.isEmpty()){
                        currentView.setBackgroundResource(R.drawable.rounded_rect_line)
                    }
                }
            }
        }
    }
}
