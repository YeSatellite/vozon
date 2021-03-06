package com.yesat.vozon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.yesat.vozon.R
import com.yesat.vozon.models.Country
import com.yesat.vozon.utility.Shared
import kotlinx.android.synthetic.main.item_code_selected.view.*
import kotlinx.android.synthetic.main.item_info_tmp.view.*

class CodeAdapter(val context: Context, val list: List<Country>): BaseAdapter(){
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = View.inflate(context, R.layout.item_code_selected,null)
        v.v_code.text = list[position].phoneCode!!
        return v
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val v = View.inflate(context, R.layout.item_info_tmp2,null)
        v.v_transport.text = list[position].name!!

        return v
    }

    override fun getItem(position: Int) = list[position]
    override fun getItemId(position: Int) = 0L
    override fun getCount() = list.size
}
