package com.example.weshare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.weshare.R
import com.example.weshare.databinding.ItemGroupBinding
import com.example.weshare.group.Group

class GroupAdapter(context: Context, groups: List<Group>) :
    ArrayAdapter<Group>(context, 0, groups) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_group, parent, false)
        val group = getItem(position)

        val groupNameTextView = view.findViewById<TextView>(R.id.groupNameTextView)
        groupNameTextView.text = group?.name

        return view
    }

}
