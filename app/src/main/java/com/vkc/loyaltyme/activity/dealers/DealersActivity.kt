package com.vkc.loyaltyme.activity.dealers

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vkc.loyaltyapp.util.CustomToast
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.dealers.adapter.DealersListAdapter
import com.vkc.loyaltyme.activity.dealers.model.dealers.Data
import com.vkc.loyaltyme.activity.dealers.model.dealers.DealersModel
import com.vkc.loyaltyme.activity.dealers.model.dealers.Response
import com.vkc.loyaltyme.api.ApiClient
import com.vkc.loyaltyme.app_controller.AppController
import com.vkc.loyaltyme.manager.HeaderManager
import com.vkc.loyaltyme.manager.PreferenceManager
import com.vkc.loyaltyme.utils.UtilityMethods
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.lang.reflect.Executable

class DealersActivity : AppCompatActivity() {
    lateinit var context: Activity
    lateinit var dealersRecyclerList: RecyclerView
    lateinit var headerManager: HeaderManager
    lateinit var header: LinearLayout
    lateinit var imageBack: ImageView
    lateinit var editSearch: EditText
    lateinit var dealersList: ArrayList<Data>
    lateinit var tempDealer: ArrayList<Data>
    lateinit var textSubmit: TextView
    lateinit var adapter: DealersListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealers)
        context = this
        initialiseUI()
        getDealers("")
    }

    private fun getDealers(searchKey: String) {
        var dealersMainResponse: DealersModel
        var dealersResponse: Response
        var dealersAssigned: ArrayList<Data> = ArrayList()
        var dealersNotAssigned: ArrayList<Data> = ArrayList()
        dealersList.clear()
        AppController.dealersList.clear()
        if (UtilityMethods.checkInternet(context)){
            ApiClient.getApiService().getDealersResponse(
                PreferenceManager.getCustomerID(context),
                PreferenceManager.getUserType(context),
                searchKey
//            "23703","5",""
            ).enqueue(object : Callback<DealersModel>{
                override fun onResponse(
                    call: Call<DealersModel>,
                    response: retrofit2.Response<DealersModel>
                ) {
                    if (response.body() != null){
                        dealersMainResponse = response.body()!!
                        dealersResponse = dealersMainResponse.response
                        Log.e("Dealers",dealersResponse.toString())
                        if (dealersResponse.status.equals("Success")){
                            for (i in dealersResponse.data.indices){
                                var tempModel: Data = Data("","","","")
                                tempModel.id = dealersResponse.data[i].id
                                tempModel.name = dealersResponse.data[i].name
                                tempModel.role = dealersResponse.data[i].role
                                tempModel.is_assigned = dealersResponse.data[i].is_assigned
                                if (dealersResponse.data[i].is_assigned.equals("0")){
                                    dealersNotAssigned.add(tempModel)
                                }else{
                                    dealersAssigned.add(tempModel)
                                }
                            }
                            dealersList.addAll(dealersAssigned)
                            dealersList.addAll(dealersNotAssigned)
                            Log.e("Dealers1",dealersList.toString())
                            adapter = DealersListAdapter(context, dealersList)
                            dealersRecyclerList.hasFixedSize()
                            dealersRecyclerList.layoutManager = LinearLayoutManager(
                                context,
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            Log.e("Dealers2",dealersList.toString())

                            dealersRecyclerList.adapter = adapter
                        }else{
                            /****    * 
                             * Empty *
                             *   ****/
                        }
                    }else{
                        CustomToast.customToast(context)
                        CustomToast.show(0)
                    }
                }

                override fun onFailure(call: Call<DealersModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }else{
            CustomToast.customToast(context)
            CustomToast.show(58)
        }
    }

    private fun initialiseUI() {
        dealersRecyclerList = findViewById(R.id.recyclerViewDealer)
        header = findViewById(R.id.header)
        headerManager = HeaderManager(this@DealersActivity, resources.getString(R.string.dealers))
        headerManager.getHeader(header, 1)
        imageBack = headerManager.leftButton!!
        headerManager.setButtonLeftSelector(
            R.drawable.back,
            R.drawable.back
        )

        editSearch = findViewById(R.id.editSearch)
        dealersList = ArrayList()
        tempDealer = ArrayList()
        textSubmit = findViewById(R.id.textSubmit)
        imageBack.setOnClickListener {
            finish()
        }
        textSubmit.setOnClickListener {
            //listIds.clear();
            val jsonObject = JSONObject()
            val jsonArray = JSONArray()
            for (i in 0 until AppController.dealersList.size) {
                if (AppController.dealersList[i].is_assigned.toBoolean()) {


                    val `object` = JSONObject()
                    try {
                        `object`.put(
                            "id", AppController.dealersList[i].id
                        )
                        `object`.put(
                            "role", AppController.dealersList[i].role
                        )
                        jsonArray.put(`object`)
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                    }
                }
            }
            try {
                jsonObject.put("dealers", jsonArray)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (jsonArray.length() > 0) {
                if (jsonArray.length() > 10) {
                    CustomToast.customToast(context)
                    CustomToast.show(11)
                } else {
                    submitDealers(jsonArray)
                }
            } else {
                CustomToast.customToast(context)
                CustomToast.show(10)
            }
        }
//        editSearch.setOnKeyListener { v, keyCode, event ->
//            if (event.action == KeyEvent.ACTION_DOWN) {
//                when(keyCode){
//                    KeyEvent.KEYCODE_DPAD_CENTER -> true
//                    KeyEvent.KEYCODE_ENTER -> true
//                }
//            }
//            false
//        }
        editSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                try{
                    if (s!!.isEmpty()) {
                        dealersRecyclerList.scrollToPosition(0)
                    } else {
                        for (i in dealersList.indices) {
                            if (dealersList[i].name.lowercase()
                                    .startsWith(s.toString().lowercase())
                            ) {
                                dealersRecyclerList.scrollToPosition(i)
                                break
                            }
                        }
                    }
                }catch (e:Exception){
                    Log.e("String",e.toString())
                }
            }

        })
    }

    private fun submitDealers(jsonArray: JSONArray) {
        /***************
         * not
         * found
         * in server
         ***************/

    }
}