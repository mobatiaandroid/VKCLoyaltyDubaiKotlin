package com.vkc.loyaltyme.activity.point_history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vkc.loyaltyme.R
import com.vkc.loyaltyme.activity.point_history.constants.Constants
import com.vkc.loyaltyme.activity.point_history.model.transaction.TransactionHistory
import com.vkc.loyaltyme.activity.point_history.model.transaction_new.TransactionModelNew
import com.vkc.loyaltyme.app_controller.AppController

class TransactionHistoryAdapterNew() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.PARENT) {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history_parent, parent, false)
            GroupViewHolder(rowView)
        } else {
            val rowView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history_child, parent, false)
            ChildViewHolder(rowView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataList = AppController.transactionDataNew[position]
        if (dataList.type == Constants.PARENT) {
            holder as GroupViewHolder
            holder.apply {
                textUser.text = AppController.transactionData[position].to_name
                val points = AppController.transactionData[position].tot_points
                textPoint.text = "$points Coupons"
                holder.itemView?.setOnClickListener {
                    expandOrCollapseParentItem(dataList, position)
                }
            }
        } else {
            holder as ChildViewHolder

            holder.apply {
                val singleService = dataList.details.first()
//                if (childPosition % 2 == 1) {
//                    currentChildView.setBackgroundColor(
//                        parent!!.context.resources.getColor(
//                            R.color.list_row_color_grey
//                        )
//                    )
//                } else {
//                    currentChildView.setBackgroundColor(
//                        parent!!.context.resources.getColor(
//                            R.color.list_row_color_white
//                        )
//                    )
//                }

                textType.text = singleService.type
                textPoints.text = singleService.points
                if (singleService.to_name.isNotEmpty()) {
                    textToUser.text = (singleService.to_name + " / " + singleService.to_role)
                } else {
                    textToUser.text = ""
                }
                textDate.text = singleService.date
            }
        }
    }

    private fun expandOrCollapseParentItem(
        singleBoarding: TransactionModelNew.Response.TransactionHistory,
        position: Int,
    ) {

        if (singleBoarding.isExpanded) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }

    private fun expandParentRow(position: Int) {
        val currentBoardingRow = AppController.transactionDataNew[position]
        val services = currentBoardingRow.details
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if (currentBoardingRow.type == Constants.PARENT) {

            services.forEach { service ->
                val parentModel =
                    TransactionModelNew.Response.TransactionHistory(ArrayList(), "", "")
                parentModel.type = Constants.CHILD
                val subList: ArrayList<TransactionModelNew.Response.TransactionHistory.IndividualTransaction> =
                    ArrayList()
                subList.add(service)
                parentModel.details = subList
                AppController.transactionDataNew.add(++ nextPosition, parentModel)
            }
            notifyDataSetChanged()
        }
    }

    private fun collapseParentRow(position: Int) {
        val currentBoardingRow = AppController.transactionDataNew[position]
        val services = currentBoardingRow.details
        AppController.transactionDataNew[position].isExpanded = false
        if (AppController.transactionDataNew[position].type == Constants.PARENT) {
            services.forEach { _ ->
                AppController.transactionDataNew.removeAt(position + 1)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int =
        AppController.transactionDataNew[position].type

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class GroupViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val textUser = row.findViewById<View>(R.id.textUser) as TextView
        val textPoint = row.findViewById<View>(R.id.textPoint) as TextView

        val textIcon = row.findViewById<View>(R.id.textIcon) as TextView

    }

    class ChildViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        //        val childTV = row.findViewById(R.id.child_Title) as TextView?
        val textType = row.findViewById<View>(R.id.textType) as TextView
        val textPoints = row.findViewById<View>(R.id.textPoints) as TextView
        val textToUser = row.findViewById<View>(R.id.textToUser) as TextView
        val textDate = row.findViewById<View>(R.id.textDate) as TextView
    }


    override fun getItemCount(): Int {
        return AppController.transactionData.size
    }
}