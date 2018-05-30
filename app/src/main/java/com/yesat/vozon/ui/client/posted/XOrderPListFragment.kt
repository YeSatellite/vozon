package com.yesat.vozon.ui.client.posted

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import com.yesat.car.utility.ui.ListFragment
import com.yesat.vozon.*
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.client.OrderNewActivity
import com.yesat.vozon.ui.client.XOrderDetailActivity
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_posted_order.view.*


class XOrderPListFragment : ListFragment<Order, XOrderPListFragment.ViewHolder>() {
    companion object {
        const val OFFER_LIST_ACTIVITY = 25
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.clientService.orders(Shared.posted).run2(srRefresh,{ body ->
            norm(body.size.toString())
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_posted_order, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hTitle = v.v_title!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
        val hPosition = v.v_position!!
        val hShowOffers = v.v_show_offers!!
        val hImage= v.v_image!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Order) {
        holder.hTitle.text = item.title
        holder.hStartPoint.text = item.startPoint!!.getShortName()
        holder.hEndPoint.text = item.endPoint!!.getShortName()
        holder.hPosition.text = item.startPoint!!.getShortName()
        holder.hImage.src = item.image1
        holder.hShowOffers.setOnClickListener({
            val i = Intent(activity, XOfferListActivity::class.java)
            i.put(item)
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
                val i = Intent(activity, OrderNewActivity::class.java)
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
        val i = Intent(activity, XOrderDetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }
}
