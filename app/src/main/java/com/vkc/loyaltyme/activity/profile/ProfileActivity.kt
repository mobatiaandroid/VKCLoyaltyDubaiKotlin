package com.vkc.loyaltyme.activity.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.vkc.loyaltyme.BuildConfig
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.common.SignUpActivity
import com.vkc.loyaltyme.activity.common.model.verify_otp.VerifyOTPModel
import com.vkc.loyaltyme.activity.dealers.DealersActivity
import com.vkc.loyaltyme.activity.home.HomeActivity
import com.vkc.loyaltyme.activity.profile.model.profile.Data
import com.vkc.loyaltyme.activity.profile.model.profile.ProfileModel
import com.vkc.loyaltyme.activity.profile.model.update_phone.Response
import com.vkc.loyaltyme.activity.profile.model.update_phone.UpdatePhoneModel
import com.vkc.loyaltyme.activity.profile.model.update_profile.UpdateProfileModel
import com.vkc.loyaltyme.api.ApiClient
import com.vkc.loyaltyme.manager.HeaderManager
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.CustomToast
import com.vkc.loyaltyme.utils.ProgressBarDialog
import com.vkc.loyaltyme.utils.UtilityMethods
import id.zelory.compressor.Compressor
import id.zelory.compressor.FileUtil
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt


class ProfileActivity : AppCompatActivity() {
    lateinit var context: Activity
    lateinit var headerManager: HeaderManager
    lateinit var header: LinearLayout
    lateinit var buttonUpdate: Button
    lateinit var editMobile: EditText
    lateinit var editOwner: EditText
    lateinit var editShop: EditText
    lateinit var editState: EditText
    lateinit var editDistrict: EditText
    lateinit var editPlace: EditText
    lateinit var editPin: EditText
    lateinit var editAddress: EditText
    lateinit var editMobile2: EditText
    lateinit var editEmail: EditText
    lateinit var textCustomerID: TextView
    lateinit var textMyDealers: TextView
    lateinit var textUpdate: TextView
    lateinit var imageBack: ImageView
    lateinit var imageProfile: ImageView
    lateinit var progressBarDialog: ProgressBarDialog
    lateinit var fileCameraResult: File
    lateinit var fileGalleryResult: File
    lateinit var compressCameraResult: File
    lateinit var compressGalleryResult: File
    var outputFileUri: Uri? = null
    var imageCaptureUri: Uri? = null
    var otpValue = ""
    var filePath = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        context = this
        initialiseUI()
        getProfile()
    }

    private fun initialiseUI() {
        header = findViewById(R.id.header)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        editMobile = findViewById(R.id.editMobile)
        editOwner = findViewById(R.id.editOwner)
        editShop = findViewById(R.id.editShop)
        editState = findViewById(R.id.editState)
        editDistrict = findViewById(R.id.editDistrict)
        editPlace = findViewById(R.id.editPlace)
        editPin = findViewById(R.id.editPin)
        editAddress = findViewById(R.id.editAddress)
        editMobile2 = findViewById(R.id.editMobile2)
        editEmail = findViewById(R.id.editEmail)
        textCustomerID = findViewById(R.id.textCustId)
        textMyDealers = findViewById(R.id.textMydealers)
        textUpdate = findViewById(R.id.textUpdate)
        imageProfile = findViewById(R.id.imageProfile)
        progressBarDialog = ProgressBarDialog(context)
        headerManager = HeaderManager(this@ProfileActivity, resources.getString(R.string.profile))
        headerManager.getHeader(header, 1)
        imageBack = headerManager.leftButton!!


        headerManager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )
        editOwner.isEnabled = true
        editShop.isEnabled = false
        editState.isEnabled = false
        editDistrict.isEnabled = false
        editPlace.isEnabled = true
        editPin.isEnabled = false
        editAddress.isEnabled = false
        buttonUpdate.setOnClickListener {
            updateProfile()
        }
        textMyDealers.setOnClickListener {
            startActivity(
                Intent(
                    this@ProfileActivity,
                    DealersActivity::class.java
                )
            )
        }

        textUpdate.setOnClickListener {
            if (editMobile.text.toString().trim().isNotEmpty()) {
                if (editMobile.text.toString().trim().length == 10) {
                    if (PreferenceManager.getMobile(context)
                            .equals(editMobile.text.toString().trim())
                    ) {

                    } else {
                        alertUpdateMobile(context)
                    }
                } else {
                    CustomToast.customToast(context)
                    CustomToast.show(54)
                }
            }
        }
        imageBack.setOnClickListener {
            val intent = Intent(context,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        imageProfile.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context,
                        Manifest.permission.CAMERA
                    )
                ) {

                    ActivityCompat.requestPermissions(
                        this@ProfileActivity,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        100
                    )
                } else {


                    ActivityCompat.requestPermissions(
                        this@ProfileActivity,
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        100
                    )

                }
            } else {
                showCameraGalleryChoice()
            }
        }
    }

    private fun updateProfile() {
        var updateProfileMainResponse: UpdateProfileModel
        var updateProfileResponse: com.vkc.loyaltyme.activity.profile.model.update_profile.Response
        if (UtilityMethods.checkInternet(context)){
            progressBarDialog.show()
            var requestFile: RequestBody? = null
            var profilePic: MultipartBody.Part? = null
            var file: File = File(filePath)
            if (file.length() > 0) {
                requestFile =
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                profilePic = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
            val customerID: RequestBody = PreferenceManager.getCustomerID(context)
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val role: RequestBody = PreferenceManager.getUserType(context)
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val mobileNo: RequestBody = editMobile.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val ownerName: RequestBody = editOwner.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val place: RequestBody = editPlace.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val mobileNo2: RequestBody = editMobile2.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val email: RequestBody = editEmail.text.toString().trim()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            if (profilePic != null) {
                ApiClient.getApiService().getUpdateProfileResponse(
                    customerID,
                    role,
                    mobileNo,
                    ownerName,
                    place,
                    mobileNo2,
                    email,
                    profilePic!!
                ).enqueue(object : Callback<UpdateProfileModel> {
                    override fun onResponse(
                        call: Call<UpdateProfileModel>,
                        response: retrofit2.Response<UpdateProfileModel>,
                    ) {
                        progressBarDialog.hide()

                        updateProfileMainResponse = response.body()!!
                        updateProfileResponse = updateProfileMainResponse.response
                        if (updateProfileResponse.status.equals("Success")){
                            CustomToast.customToast(context)
                            CustomToast.show(26)
                        }else{
                            CustomToast.customToast(context)
                            CustomToast.show(27)
                        }
                    }

                    override fun onFailure(call: Call<UpdateProfileModel>, t: Throwable) {
                        progressBarDialog.hide()
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                        Log.e("error43564", t.toString())
                    }

                })
            }
        }else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }


    private fun getProfile() {
        var profileMainResponse: ProfileModel
        var profileResponse: com.vkc.loyaltyme.activity.profile.model.profile.Response
        var profileData: Data
        if (UtilityMethods.checkInternet(context)) {
            progressBarDialog.show()
            ApiClient.getApiService().getProfileResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context)
            ).enqueue(object : Callback<ProfileModel> {
                override fun onResponse(
                    call: Call<ProfileModel>,
                    response: retrofit2.Response<ProfileModel>,
                ) {
                    progressBarDialog.hide()
                    if (response.body() != null) {
                        profileMainResponse = response.body()!!
                        profileResponse = profileMainResponse.response
                        if (profileResponse.status.equals("Success")) {
                            profileData = profileResponse.data
                            val name: String = profileData.name
                            val owner: String = profileData.contact_person
                            val district: String = profileData.district
                            val city: String = profileData.city
                            val state: String = profileData.state_name
                            val pincode: String = profileData.pincode
                            val phone: String = profileData.phone
                            val url: String = profileData.image
                            val mobile2: String = profileData.phone2
                            val email: String = profileData.email
                            editMobile2.setText(mobile2)
                            editEmail.setText(email)
                            editShop.setText(name)
                            editOwner.setText(owner)
                            editDistrict.setText(district)
                            editMobile.setText(phone)
                            editPlace.setText(city)
                            editState.setText(state)
                            editPin.setText(pincode)
                            editAddress.setText(profileData.address)
                            textCustomerID.text = "CUST_ID: - " + profileData.customer_id
                            Glide.with(context).load(url).placeholder(R.drawable.profile_image)
                                .into(imageProfile)
                        }
                    } else {
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                    progressBarDialog.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }


    private fun alertUpdateMobile(context: Activity) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_update)
        val textYes = dialog.findViewById<View>(R.id.textYes)
        val textNo = dialog.findViewById<View>(R.id.textNo)
        textYes.setOnClickListener {
            dialog.dismiss()
            updateMobile()
        }
        textNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updateMobile() {
        var updatePhoneMainResponse: UpdatePhoneModel
        var updatePhoneResponse: Response
        if (UtilityMethods.checkInternet(context)) {
            progressBarDialog.show()
            ApiClient.getApiService().getPhoneUpdateResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context),
                editMobile.text.toString().trim()
            ).enqueue(object : Callback<UpdatePhoneModel> {
                override fun onResponse(
                    call: Call<UpdatePhoneModel>,
                    response: retrofit2.Response<UpdatePhoneModel>,
                ) {
                    progressBarDialog.hide()
                    if (response.body() != null) {
                        updatePhoneMainResponse = response.body()!!
                        updatePhoneResponse = updatePhoneMainResponse.response
                        if (updatePhoneResponse.status.equals("Success")) {
                            alertOTPDialog(context)
                        } else {
                            CustomToast.customToast(context)
                            CustomToast.show(56)
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<UpdatePhoneModel>, t: Throwable) {
                    progressBarDialog.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }


    private fun dpToPx(dp: Float): Int {
        val density: Float = context.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }

    private fun alertOTPDialog(context: Activity) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_otp_alert_no_resend)
        val editOTP1 = dialog.findViewById<View>(R.id.editOtp1) as EditText
        val editOTP2 = dialog.findViewById<View>(R.id.editOtp2) as EditText
        val editOTP3 = dialog.findViewById<View>(R.id.editOtp3) as EditText
        val editOTP4 = dialog.findViewById<View>(R.id.editOtp4) as EditText
        val textCancel = dialog.findViewById<View>(R.id.textCancel) as TextView
        val textOTP = dialog.findViewById<View>(R.id.textOtp) as TextView
        val buttonCancel = dialog.findViewById<View>(R.id.buttonCancel) as Button
        val mob = PreferenceManager.getMobile(context)!!.substring(6, 10)
        textOTP.text = getString(R.string.otp_sent, mob)
        editOTP1.isCursorVisible = false
        textCancel.setOnClickListener {
            dialog.dismiss()
        }


        editOTP1.addTextChangedListener(SignUpActivity.GenericTextWatcher(editOTP1, editOTP2))
        editOTP2.addTextChangedListener(SignUpActivity.GenericTextWatcher(editOTP2, editOTP3))
        editOTP3.addTextChangedListener(SignUpActivity.GenericTextWatcher(editOTP3, editOTP4))
        editOTP4.addTextChangedListener(SignUpActivity.GenericTextWatcher(editOTP4, null))

        editOTP1.setOnKeyListener(SignUpActivity.GenericKeyEvent(editOTP1, null))
        editOTP2.setOnKeyListener(SignUpActivity.GenericKeyEvent(editOTP2, editOTP1))
        editOTP3.setOnKeyListener(SignUpActivity.GenericKeyEvent(editOTP3, editOTP2))
        editOTP4.setOnKeyListener(SignUpActivity.GenericKeyEvent(editOTP4, editOTP3))

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        editOTP4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 1) {
                    if (editOTP1.text.isNotEmpty()
                        && editOTP2.text.isNotEmpty()
                        && editOTP3.text.isNotEmpty()
                        && editOTP4.text.isNotEmpty()
                    ) {
                        otpValue = editOTP1.text.toString().trim() + editOTP2.text.toString()
                            .trim() + editOTP3.text.toString().trim() + editOTP4.text.toString()
                            .trim()
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
            progressBarDialog.show()
            ApiClient.getApiService().getVerifyOTPResponse(
                otpValue,
                PreferenceManager.getUserType(context),
                PreferenceManager.getCustomerID(context),
                mobileNo,
                "0"
            ).enqueue(object : Callback<VerifyOTPModel> {
                override fun onResponse(
                    call: Call<VerifyOTPModel>,
                    response: retrofit2.Response<VerifyOTPModel>,
                ) {
                    progressBarDialog.hide()
                    if (response.body() != null) {
                        verifyOTPMainResponse = response.body()!!
                        verifyOTPResponse = verifyOTPMainResponse.response
                        if (verifyOTPResponse.status.equals("Success")) {
                            CustomToast.customToast(context)
                            CustomToast.show(55)
                            val intent = Intent(
                                this@ProfileActivity,
                                SignUpActivity::class.java
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)

                        } else {

                        }
                    } else {
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<VerifyOTPModel>, t: Throwable) {
                    progressBarDialog.hide()
                    CustomToast.customToast(context)
                    CustomToast.show(0)
                }

            })
        } else {
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }



    private fun showCameraGalleryChoice() {
        val dialogGetImageFrom = AlertDialog.Builder(context)
        dialogGetImageFrom.setTitle(resources.getString(R.string.select_item))
        val options = arrayOf<CharSequence>(
            context.resources.getString(R.string.take_picture),
            context.resources.getString(R.string.open_gallery)
        )
        dialogGetImageFrom.setItems(
            options
        ) { dialog, which ->
            when(which){
             0 -> {
                 try{
                     val imageFileName =
                            System.currentTimeMillis().toString() + ".jpg"
                        var storageDir: File = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                        filePath = storageDir.absolutePath + "/" + imageFileName
                        Log.e("PICTUREPATH : ", filePath)
                        val file = File(filePath)
                        outputFileUri = FileProvider.getUriForFile(
                            this,
                            BuildConfig.APPLICATION_ID + "." + localClassName + ".provider",
                            file);
                        val cameraIntent =
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                        startActivityForResult(cameraIntent, 0)
                 }catch (e: Exception){
                     Log.e("Error",e.toString())
                 }
             }
             1 -> {
                 val pickPhoto = Intent(Intent.ACTION_PICK,
                         MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                 startActivityForResult(Intent.createChooser(pickPhoto,resources.getString(
                            R.string.select_item)), 1)

             }
            }
        }
        dialogGetImageFrom.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            fileCameraResult = File(filePath)
            if (fileCameraResult.exists()) {
                try {
                    fileCameraResult = FileUtil.from(context, outputFileUri)
                    compressCameraResult = Compressor.Builder(context)
                        .setMaxWidth(940f)
                        .setMaxHeight(800f)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES
                            ).absolutePath
                        )
                        .build()
                        .compressToFile(fileCameraResult)
                    Glide.with(context).load(outputFileUri).placeholder(R.drawable.profile_image)
                        .into(imageProfile)
                    filePath = compressCameraResult.path
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(context, "Cannot show image", Toast.LENGTH_SHORT).show()
                return
            }
            try {
                fileGalleryResult = FileUtil.from(context, data.data)
                compressGalleryResult = Compressor.Builder(context)
                    .setMaxWidth(940f)
                    .setMaxHeight(800f)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        ).absolutePath
                    )
                    .build()
                    .compressToFile(fileGalleryResult)
                Glide.with(context).load(data.data).placeholder(R.drawable.profile_image)
                    .into(imageProfile)
                Log.e("path", fileGalleryResult.path)
                filePath = compressGalleryResult.path
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }




    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(context,HomeActivity::class.java)
        startActivity(intent)
    }

}