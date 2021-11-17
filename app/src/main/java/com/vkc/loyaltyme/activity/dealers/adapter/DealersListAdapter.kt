package com.vkc.loyaltyme.activity.dealers.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.app_controller.AppController
import com.vkc.loyaltyme.activity.dealers.model.dealers.Data

class DealersListAdapter(var context: Context, var dealersList: ArrayList<Data>)
    : RecyclerView.Adapter<DealersListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var textName: TextView? = null
        var checkBox: CheckBox? = null

        init {
            textName = itemView.findViewById(R.id.textViewName)
            checkBox = itemView.findViewById(R.id.checkbox_dealer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dealers_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textName!!.text = dealersList[position].name
        holder.checkBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                dealersList[position].is_assigned = "1"
            }else{
                dealersList[position].is_assigned = "0"
            }
        }
        holder.checkBox!!.isChecked =dealersList[position].is_assigned.equals("1")
    }

    override fun getItemCount(): Int {
        return dealersList.size
    }
}
