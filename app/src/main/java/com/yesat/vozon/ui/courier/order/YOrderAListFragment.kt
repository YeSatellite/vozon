package com.yesat.vozon.ui.courier.order

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.R
import com.yesat.vozon.models.Order
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_courier_order.view.*


class YOrderAListFragment : ListFragment<Order, YOrderAListFragment.ViewHolder>() {

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.courierService.orders(Shared.active).run2(srRefresh,{body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_courier_order, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hTitle = v.v_title!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
        val hPosition = v.v_t_type!!
        val hImage= v.c_image!!
        val hAvatar= v.v_avatar!!
        val hDate= v.v_date!!
        val hName= v.v_transport!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Order) {
        holder.hTitle.text = item.title
        holder.hStartPoint.text = item.startPoint?.getShortName()
        holder.hEndPoint.text = item.endPoint?.getShortName()
        holder.hPosition.text = item.startPoint?.getShortName()
        holder.hImage.src(item.image1,R.drawable.tmp)
        holder.hAvatar.src(item.owner?.avatar,R.drawable.user_placeholder)
        holder.hDate.text = item.shippingDate
        holder.hName.text = item.owner?.name
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
//            refreshListener.onRefresh()
        }

    }

    override fun onItemClick(item: Order) {
        val i = Intent(activity, YOrderADetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }
}
