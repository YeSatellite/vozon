package com.yesat.vozon.ui.courier.transport

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.R
import com.yesat.vozon.models.Transport
import com.yesat.vozon.ui.ListActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.item_transport.view.*


class TransportListActivity: ListActivity<Transport, TransportListActivity.ViewHolder>() {

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.courierService.transports().run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transport, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        //        val hImage = v.v_image!!
        val hName = v.v_transport!!
        val hNumber= v.v_number!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Transport) {
        holder.hName.text = item.fullName
        holder.hNumber.text = item.number
    }

    override fun onItemClick(item: Transport){
        val i = Intent()
        i.put(item)
        setResult(Activity.RESULT_OK,i)
        finish()
    }
}










