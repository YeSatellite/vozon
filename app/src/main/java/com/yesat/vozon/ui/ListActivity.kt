package com.yesat.vozon.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.R
import com.yesat.vozon.utility.Shared
import com.yesat.vozon.utility.norm
import kotlinx.android.synthetic.main.fragment_list.*

abstract class ListActivity<T,V : ListFragment.ViewHolder>: AppCompatActivity() {
    var refreshListener: SwipeRefreshLayout.OnRefreshListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Shared.theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_list)

        val adapter = ListAdapter()
        v_list.adapter = adapter
        val srRefresh = v_refresh
        norm(">>>>>>>>>>")

        refreshListener = SwipeRefreshLayout.OnRefreshListener({
            refreshListener(adapter,srRefresh)
        })


        srRefresh.setOnRefreshListener(refreshListener)

        srRefresh.post({
            refreshListener!!.onRefresh()
            srRefresh.isRefreshing = true
        })
        srRefresh.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.BLUE)
    }

    inner class ListAdapter: RecyclerView.Adapter<V>() {
        var list: List<T> = java.util.ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
            return onCreateViewHolder2(parent)
        }

        override fun onBindViewHolder(holder: V, position: Int) {
            onBindViewHolder2(holder,list[position])

            holder.v.setOnClickListener {
                onItemClick(list[position])
            }
        }

        override fun getItemCount() = list.size
    }

    abstract class ViewHolder(var v: View) : RecyclerView.ViewHolder(v)

    abstract fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout)
    abstract fun onCreateViewHolder2(parent: ViewGroup): V
    abstract fun onBindViewHolder2(holder: V,item : T)
    open fun onItemClick(item: T){}
}
