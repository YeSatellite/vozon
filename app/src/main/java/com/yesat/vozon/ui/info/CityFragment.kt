package com.yesat.car.ui.info

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.car.utility.ui.ListFragment
import com.yesat.vozon.R
import com.yesat.vozon.models.Location
import com.yesat.vozon.run2
import com.yesat.vozon.put
import com.yesat.vozon.snack
import kotlinx.android.synthetic.main.fragment_xprofile.view.*
import retrofit2.Call



@SuppressLint("ValidFragment")
class CityFragment(val call: Call<List<Location>>) : ListFragment<Location, CityFragment.ViewHolder>() {


    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        call.run2(srRefresh,{body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
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
    override fun onBindViewHolder2(holder: ViewHolder, item: Location) {
        holder.hName.text = item.name
    }

    override fun onItemClick(item: Location){
        val i = Intent()
        i.put(item)
        activity!!.setResult(Activity.RESULT_OK,i)
        activity!!.finish()
    }
}
