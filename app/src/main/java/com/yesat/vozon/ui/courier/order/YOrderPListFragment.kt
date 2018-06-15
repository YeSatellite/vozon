package com.yesat.vozon.ui.courier.order

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import com.yesat.vozon.R
import com.yesat.vozon.models.Location
import com.yesat.vozon.models.Order
import com.yesat.vozon.models.Route
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.ui.client.route.RouteFilterActivity
import com.yesat.vozon.ui.client.route.XRouteListFragment
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_courier_order.view.*


class YOrderPListFragment : ListFragment<Order, YOrderPListFragment.ViewHolder>() {


    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        if(Shared.filter.isEmpty()) Shared.filter.add(Shared.currentUser.city!!)
        val filter = Shared.filter.joinToString(separator = ",",transform = {it.id.toString()})
        Api.courierService.orders(Shared.posted,filter).run2(srRefresh,{body ->
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
        holder.hImage.src(if(item.image1 != null) item.image1 else item.image2,R.drawable.tmp)
        holder.hAvatar.src(item.owner?.avatar,R.drawable.user_placeholder)
        holder.hDate.text = item.shippingDate
        holder.hName.text = item.owner?.name
    }

    override fun onItemClick(item: Order) {
        val i = Intent(activity, YOrderPDetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                val i = Intent(activity, OrderFilterActivity::class.java)
                startActivityForResult(i, XRouteListFragment.ROUTE_FILTER_ACTIVITY)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        refresh!!.run()
    }
}
