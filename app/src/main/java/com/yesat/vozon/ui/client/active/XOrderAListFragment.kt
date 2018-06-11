package com.yesat.vozon.ui.client.active

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.R
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.client.CategoryActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_active_order.view.*


class XOrderAListFragment : ListFragment<Order, XOrderAListFragment.ViewHolder>() {
    companion object {
        const val OFFER_LIST_ACTIVITY = 25
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.clientService.orders(Shared.active).run2(srRefresh,{ body ->
            norm(body.size.toString())
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_active_order, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hTitle = v.v_title!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
        val hPosition = v.v_t_type!!
        val hYourCourier = v.v_your_courier!!
        val hImage= v.v_image!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Order) {
        holder.hTitle.text = item.title
        holder.hStartPoint.text = item.startPoint!!.getShortName()
        holder.hEndPoint.text = item.endPoint!!.getShortName()
        holder.hPosition.text = item.startPoint!!.getShortName()
        holder.hImage.src(item.image1,R.drawable.tmp)
        holder.hYourCourier.setOnClickListener({
            val i = Intent(activity,CourierProfileActivity::class.java)
            i.put(item.offer!!.transport!!.owner!!)
            startActivity(i)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                val i = Intent(activity, CategoryActivity::class.java)
                startActivityForResult(i,32)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OFFER_LIST_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                refreshListener!!.onRefresh()
            }
        }
    }

    override fun onItemClick(item: Order) {
        val i = Intent(activity, XOrderADetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }
}
