package com.yesat.vozon.ui.client

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import com.yesat.vozon.R
import com.yesat.vozon.models.Route
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.ui.ListToolbarFragment
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_client_route.view.*


class XRouteListFragment : ListToolbarFragment<Route, XRouteListFragment.ViewHolder>() {
    companion object {
        const val ROUTE_FILTER_ACTIVITY = 25
    }

    private var filter: Route.FilterRoute? = null

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        filter = Route  .FilterRoute(1,1,1,
                "2010-1-1","2020-1-1")
        if (filter == null){
            srRefresh.snack("asdf")
            srRefresh.post {
                srRefresh.isRefreshing = false
            }
            return
        }
        Api.clientService.routes(
                filter!!.type!!,
                filter!!.startPoint!!,
                filter!!.endPoint!!,
                filter!!.startDate!!,
                filter!!.endDate!!
        ).run2(srRefresh,{body ->
            norm("size: ${body.size}")
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            srRefresh.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_client_route, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hTransport = v.v_transport!!
        val hTType = v.v_t_type!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
        val hName = v.v_name!!
        val hAvatar = v.v_avatar!!
        val hRating = v.v_rating!!
        val hDate = v.v_date!!


    }

    override fun onBindViewHolder2(holder: ViewHolder, item: Route) {
        holder.hTransport.text = item.transport?.fullName
        holder.hTType.text = "asdf" //TODO
        holder.hStartPoint.text = item.startPoint!!.getShortName()
        holder.hEndPoint.text = item.endPoint!!.getShortName()
        holder.hName.text =item.transport?.owner?.name
        holder.hAvatar.src =item.transport?.owner?.avatar
        holder.hRating.text =item.transport?.owner?.rating
        holder.hDate.text = item.shippingDate?.dateFormat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ROUTE_FILTER_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                filter = data!!.get(Route.FilterRoute::class.java)
                refreshListener!!.onRefresh()
            }
        }
    }
}
