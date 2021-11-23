package com.vkc.loyaltyme.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.TextView
import android.widget.Toast
import com.vkc.loyaltyme.R

@SuppressLint("StaticFieldLeak")
object CustomToast {

    lateinit var mActivity: Activity
    lateinit var mTextView: TextView
    lateinit var mToast: Toast

    fun customToast(mActivity : Activity) {
        CustomToast.mActivity = mActivity

        init()

    }

    fun init() {
        val inflater = mActivity.layoutInflater
        val layouttoast = inflater.inflate(R.layout.custom_toast, null)
        mTextView = layouttoast.findViewById(R.id.texttoast)
        mToast = Toast(mActivity)
        mToast.view = layouttoast
    }

    fun show(errorCode: Int) {
        if (errorCode == 0) {
            mTextView.text = mActivity.resources.getString(
                R.string.common_error
            )
        }
        if (errorCode == 1) {
            mTextView.text = "Successfully logged in."
        }
        if (errorCode == 2) {
            mTextView.text = "Login failed.Please try again later"
        }
        if (errorCode == 3) {
            mTextView.text = "Successfully submitted login request"
        }
        if (errorCode == 4) {
            mTextView.text = "Invalid user type"
        }
        if (errorCode == 5) {
            mTextView.text = "No results found"
        }
        if (errorCode == 6) {
            mTextView.text = "Successfully added to cart"
        }
        if (errorCode == 7) {
            mTextView.text = "Already redeemed this gift"
        }
        if (errorCode == 8) {
            mTextView.text = "OTP Verification Successful"
        }
        if (errorCode == 9) {
            mTextView.text = "Incorrect OTP"
        }
        if (errorCode == 10) {
            mTextView.text = "Please select dealers"
        }
        if (errorCode == 11) {
            mTextView.text = "Cannot assign more than 10 Dealers"
        }
        if (errorCode == 12) {
            mTextView.text = "Dealers added successfully"
        }
        if (errorCode == 13) {
            mTextView.text = "No record found"
        }
        if (errorCode == 14) {
            mTextView.text = "Please select a retailer"
        }
        if (errorCode == 15) {
            mTextView.text = "Please enter coupon value"
        }
        if (errorCode == 16) {
            mTextView.text = "Coupon value should not be greater than credit value"
        }
        if (errorCode == 17) {
            mTextView.text = "Please enter coupon value to issue"
        }
        if (errorCode == 18) {
            mTextView.text = "Coupon issued successfully"
        }
        if (errorCode == 19) {
            mTextView.text = "Image uploaded successfully"
        }
        if (errorCode == 20) {
            mTextView.text = "You have exceeded the image upload limit for this week"
        }
        if (errorCode == 21) {
            mTextView.text = "Please capture an image to upload"
        }
        if (errorCode == 22) {
            mTextView.text = "Insufficient coupon balance to redeem the gift"
        }
        if (errorCode == 23) {
            mTextView.text = "This feature is only available for retailers"
        }
        if (errorCode == 24) {
            mTextView.text = "Failed.Try again later"
        }
        if (errorCode == 25) {
            mTextView.text = "Please select a distributor"
        }
        if (errorCode == 26) {
            mTextView.text = "Profile updated successfully"
        }
        if (errorCode == 27) {
            mTextView.text = "Profile updation failed"
        }
        if (errorCode == 28) {
            mTextView.text =
                "Your registration with VKC Loyalty is on hold.Please login after verification"
        }
        if (errorCode == 29) {
            mTextView.text = "Cannot login using multiple devices. Please contact VKC"
        }
        if (errorCode == 30) {
            mTextView.text =
                "Mobile number updated successfully. Please login using new mobile number"
        }
        if (errorCode == 31) {
            mTextView.text = "This feature is currently not available."
        }
        if (errorCode == 32) {
            mTextView.text = "Please select state."
        }
        if (errorCode == 33) {
            mTextView.text = "Please select district."
        }
        if (errorCode == 34) {
            mTextView.text = "OTP sent successfully."
        }
        if (errorCode == 35) {
            mTextView.text = "Already registered with scheme"
        }
        if (errorCode == 36) {
            mTextView.text = "Please enter search key"
        }
        if (errorCode == 37) {
            mTextView.text = "Please agree terms and conditions to continue."
        }
        if (errorCode == 38) {
            mTextView.text = "Do not have enough coupons to add to cart"
        }
        if (errorCode == 39) {
            mTextView.text = "Add to cart failed"
        }
        if (errorCode == 40) {
            mTextView.text = "Please enter quantity value"
        }
        if (errorCode == 41) {
            mTextView.text = "Please select a voucher"
        }
        if (errorCode == 42) {
            mTextView.text = "Please enter the quantity value"
        }
        if (errorCode == 43) {
            mTextView.text = "No items in cart"
        }
        if (errorCode == 44) {
            mTextView.text = "No dealers found"
        }
        if (errorCode == 45) {
            mTextView.text = "Please select dealer"
        }
        if (errorCode == 46) {
            mTextView.text = "Not enough data to place order"
        }
        if (errorCode == 47) {
            mTextView.text = "Order Placed Successfully"
        }
        if (errorCode == 48) {
            mTextView.text = "Unable to place order. Try again"
        }
        if (errorCode == 49) {
            mTextView.text = "Unable to delete data. Try again"
        }
        if (errorCode == 50) {
            mTextView.text = "No messages found"
        }
        if (errorCode == 51) {
            mTextView.text = "No images uploaded this week"
        }
        if (errorCode == 52) {
            mTextView.text = "Image Deleted Successfully"
        }
        if (errorCode == 53) {
            mTextView.text = "Please enter mobile number"
        }
        if (errorCode == 54) {
            mTextView.text = "Invalid mobile number"
        }

        if (errorCode == 55) {
            mTextView.text = "Mobile number updated successfully. Please login again."
        }
        if (errorCode == 56) {
            mTextView.text = "Updation Failed."
        }
        if (errorCode == 57) {
            mTextView.text = "Please select user type"
        }
        if (errorCode == 58) {
            mTextView.text = "No internet connectivity"
        }
        if (errorCode == 59) {
            mTextView.text = "Please enter a valid quantity"
        }
        if (errorCode == 60) {
            mTextView.text = "Quantity cannot be 0"
        }
        if (errorCode == 61) {
            mTextView.text =
                "Unable to issue coupons,since there is no scheme running in your state"

        }
        if (errorCode == 62) {
            mTextView.text =
                "Unable to fetch cart count,since there is no scheme running in your state"

        }
        if (errorCode == 63) {
            mTextView.text = "Unable to update cart,since there is no scheme running in your state"

        }
        if (errorCode == 64) {
            mTextView.text = "Unable to add to cart,since there is no scheme running in your state"

        }
        if (errorCode == 65) {
            mTextView.text = "Please select retailer"

        }
        if (errorCode == 66) {
            mTextView.text = "No retailer redeem data found"

        }
        mToast.duration = Toast.LENGTH_SHORT
        mToast.show()
    }
}