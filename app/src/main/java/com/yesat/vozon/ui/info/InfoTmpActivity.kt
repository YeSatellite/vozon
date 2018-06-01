package com.yesat.vozon.ui.info

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.R
import com.yesat.vozon.utility.Shared
import com.yesat.vozon.models.InfoTmp
import com.yesat.vozon.utility.run2
import com.yesat.vozon.ui.ListActivity
import com.yesat.vozon.utility.norm
import com.yesat.vozon.utility.put
import com.yesat.vozon.utility.snack
import kotlinx.android.synthetic.main.item_info_tmp.view.*


class InfoTmpActivity: ListActivity<InfoTmp, InfoTmpActivity.ViewHolder>() {

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        norm("START")
        Shared.call?.clone()?.run2(srRefresh,{ body ->
            norm("END")
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info_tmp, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hName = v.v_name!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: InfoTmp) {
        holder.hName.text = item.name
    }

    override fun onItemClick(item: InfoTmp){
        val i = Intent()
        i.put(item)
        setResult(Activity.RESULT_OK,i)
        finish()
    }
}










