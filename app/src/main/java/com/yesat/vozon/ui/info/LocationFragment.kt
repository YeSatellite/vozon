package com.yesat.vozon.ui.info

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import android.view.*
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.R
import com.yesat.vozon.models.InfoTmp
import com.yesat.vozon.utility.norm
import com.yesat.vozon.utility.run2
import com.yesat.vozon.utility.snack
import kotlinx.android.synthetic.main.item_info_tmp.view.*
import retrofit2.Call


@SuppressLint("ValidFragment")
class LocationFragment(val call: Call<List<InfoTmp>>) : ListFragment<InfoTmp, LocationFragment.ViewHolder>() {
    var adapter: ListFragment<InfoTmp, LocationFragment.ViewHolder>.ListAdapter? = null
    var list: List<InfoTmp> = ArrayList()


    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        call.clone().run2(srRefresh,{body ->
            this.adapter = adapter
            list = body

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
        val hName = v.v_transport!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: InfoTmp) {
        holder.hName.text = item.name
    }

    override fun onItemClick(item: InfoTmp){
        norm("id: ${item.id}")
        (activity as LocationActivity).next(item.id!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.m_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (adapter != null){
                    adapter!!.list = list.filter { infoTmp ->
                        infoTmp.name!!.contains(text!!, true)
                    }
                    adapter!!.notifyDataSetChanged()
                    return true
                }
                return false
            }

        })
    }
}
