package com.vkc.loyaltyme.manager

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager {
    companion object {
        private const val PREFS_NAME = "VKC"
        const val PREFS_KEY_TYPE = "type"
        const val PREFS_KEY_OFFER = "offer"
        const val PREFS_KEY_SIZE = "size"
        const val PREFS_KEY_COLOR = "color"
        const val PREFS_KEY_PRICE = "price_range"
        const val PREFS_REG_ID = "Regid"
        const val PREFS_FILTER_DATACATEGORY = "category"
        const val PREFS_FILTER_DATASIZE = "sizefilter"
        const val PREFS_FILTER_DATAOFFER = "offerfilter"
        const val PREFS_FILTER_DATABRAND = "brandfilter"
        const val PREFS_FILTER_DATAPRICE = "pricefilter"
        const val PREFS_FILTER_DATACOLOR = "colorfilter"
        const val PREFS_BANNE_RESPONSE = "BannerResponse"
        const val PREFS_TOP_SLIDER = "top_slider"
        const val PREFS_BOTTOM_SLIDER = "bottom_slider"
        const val IDS_FOR_OFFER = "idsforoffer"
        const val OFFER_IDS = "offer_ids"
        const val LISTING_OPTION = "lising_option"
        const val BRAND_BANNER = "BrandBanner"
        const val LIST_TYPE = "ListType"
        const val BRAND_ID_FOR_SEARCH = "brand_id_for_search"
        const val MAIN_CATEGORY = "MAIN_CATEGORY"
        const val SUB_CATEGORY_ID = "SUB_CATEGORY_ID"
        const val PRODUCT_LIST_SORTOPTION = "PRODUCT_LIST_SORTOPTION"
        const val USERTYPE = "usertype"
        const val USERID = "userid"
        const val USERNAME = "username"

        fun getLoginStatusFlag(context: Context): String? {
            val text: String?
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            text = settings.getString("loginstatus", "N")
            return text
        }

        fun setLoginStatusFlag(context: Context, text: String) {
            val editor: SharedPreferences.Editor
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            editor.putString("loginstatus", text)
            editor.commit()
        }

        fun getAgreeTerms(context: Context): Boolean {
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val text: Boolean = settings.getBoolean("terms", false)
            return text
        }
        fun setAgreeTerms(context: Context, text: Boolean) {
            val editor: SharedPreferences.Editor
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            editor.putBoolean("terms", text)
            editor.commit()
        }

        fun getIsVerifiedOTP(context: Context): String {
            val text: String?
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            text = settings.getString("otp_status", "N")
            return text!!
        }
        fun setIsVerifiedOTP(context: Context, text: String?) {
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putString("otp_status", text)
            editor.commit()
        }

        fun getDealerCount(context: Context): Int {
            val text: Int
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            text = settings.getInt("dealer_count", 0)
            return text
        }

        fun setDealerCount(context: Context, text: Int) {
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putInt("dealer_count", text)
            editor.commit()
        }

        fun setMobileNo(context: Context, mobileNo: String) {
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putString("mobile", mobileNo)
            editor.commit()
        }
        fun getMobile(context: Context): String? {
            val text: String?
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            text = settings.getString("mobile", "0")
            return text
        }

        fun setCustomerID(context: Context, customerId: String) {
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = settings.edit()

            editor.putString("custId", customerId)
            editor.commit()
        }
        fun getCustomerID(context: Context): String {
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val text: String? = settings.getString("custId", "")
            return text!!
        }

        fun setUserType(context: Context, role: String) {
            // 1-SalesPerson,2-Dealer,3-Retailer
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = settings.edit()
            editor.putString(USERTYPE, role)
            editor.commit()
        }

        fun getUserType(context: Context): String {
            val text: String?
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            text = settings.getString(USERTYPE, "")
            return text!!
        }

        fun setToken(context: Context, text: String?) {
            val editor: SharedPreferences.Editor
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            editor.putString("token", text)
            editor.commit()
        }
        fun getToken(context: Context): String {
            val text: String?
            val settings: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            text = settings.getString("token", "")
            return text!!
        }

    }
}