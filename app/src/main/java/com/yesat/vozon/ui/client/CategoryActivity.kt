package com.yesat.vozon.ui.client

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.R
import com.yesat.vozon.models.Category
import com.yesat.vozon.ui.ListActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryActivity: ListActivity<Category, CategoryActivity.ViewHolder>() {

    companion object {
        const val ORDER_NEW_ACTIVITY = 36
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.infoService.category().run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hIcon = v.v_icon!!
        val hName = v.v_name!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Category) {
        holder.hIcon.src = item.icon
        holder.hName.text = item.name
    }

    override fun onItemClick(item: Category){
        val i = Intent(this, OrderNewActivity::class.java)
        i.put(item)
        startActivityForResult(i,ORDER_NEW_ACTIVITY)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ORDER_NEW_ACTIVITY){
            if (resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK, Intent())
                finish()
            }
        }
    }
}
