package com.yesat.vozon.ui.info

import android.app.Activity
import android.content.Intent
import android.icu.text.IDNA
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yesat.vozon.R
import com.yesat.vozon.models.InfoTmp
import com.yesat.vozon.ui.ListActivity
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.item_info_tmp.view.*


class InfoTmpActivity: ListActivity<InfoTmp, InfoTmpActivity.ViewHolder>(){
    var adapter: ListActivity<InfoTmp, InfoTmpActivity.ViewHolder>.ListAdapter? = null
    var list: List<InfoTmp> = ArrayList()

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        this.adapter = adapter
        norm("START")
        Shared.call?.clone()?.run2(srRefresh,{ body ->
            list = body
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
        val hName = v.v_transport!!

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchView = menu.findItem(R.id.m_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                adapter!!.list = list.filter {
                    infoTmp -> infoTmp.name!!.contains(text!!,true)
                }
                adapter!!.notifyDataSetChanged()
                return true
            }

        })

        return true
    }
}










