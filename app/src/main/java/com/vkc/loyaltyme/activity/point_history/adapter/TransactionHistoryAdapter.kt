package com.vkc.loyaltyme.activity.point_history.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter

class TransactionHistoryAdapter(mContext: Activity, listHistory: List<Any>):
    BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getGroup(groupPosition: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getGroupId(groupPosition: Int): Long {
        return 0
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        TODO("Not yet implemented")
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        TODO("Not yet implemented")
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        TODO("Not yet implemented")
    }
}