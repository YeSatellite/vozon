package com.yesat.vozon.ui.courier.order

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import com.yesat.vozon.R
import com.yesat.vozon.models.Location
import com.yesat.vozon.ui.ListActivity
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.ui.info.LocationActivity
import com.yesat.vozon.utility.Shared
import com.yesat.vozon.utility.get
import kotlinx.android.synthetic.main.item_info_tmp_r.view.*


class OrderFilterActivity: ListActivity<Location, OrderFilterActivity.ViewHolder>(){

    companion object {
        const val CITY_REQUEST_CODE = 85
    }


    var adapter: ListActivity<Location, OrderFilterActivity.ViewHolder>.ListAdapter? = null

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        this.adapter = adapter
        adapter.list = Shared.filter
        adapter.notifyDataSetChanged()
        srRefresh.post{
            srRefresh.isRefreshing= false
        }
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_info_tmp_r, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hName = v.v_transport!!
        val hRemove = v.v_remove!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Location) {
        holder.hName.text = item.name
        holder.hRemove.setOnClickListener{
            Shared.filter.remove(item)
            refresh!!.run()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                val i = Intent(this, LocationActivity::class.java)
                startActivityForResult(i, CITY_REQUEST_CODE)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CITY_REQUEST_CODE -> {
                    val location = data!!.get(Location::class.java)
                    Shared.filter.add(location)
                    refresh!!.run()
                }
            }
        }
    }
}










