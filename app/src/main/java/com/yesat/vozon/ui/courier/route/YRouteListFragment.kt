package com.yesat.vozon.ui.courier.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.yesat.vozon.R
import com.yesat.vozon.models.Route
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.ui.ListToolbarFragment
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.include_route.view.*
import kotlinx.android.synthetic.main.item_courier_route.view.*


class YRouteListFragment : ListToolbarFragment<Route, YRouteListFragment.ViewHolder>() {
    companion object {
        const val ROUTE_NEW_ACTIVITY = 25
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.courierService.routes().run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })

        (activity as AppCompatActivity).supportActionBar?.title = "Список объявлений"
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_courier_route, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hImage = v.v_image!!
        val hTransport = v.v_transport!!
        val hDate = v.v_date!!
        val hTType = v.v_t_type!!
        val hStartPoint = v.v_start_point!!
        val hEndPoint= v.v_end_point!!
    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Route) {
        holder.hImage.src(item.transport?.typeIcon,R.drawable.tmp_truck)
        holder.hImage.addFilter()
        holder.hTransport.text = item.transport?.fullName
        holder.hDate.text = item.shippingDate?.dateFormat()
        holder.hTType.text = getString(R.string._o_,item.transport?.typeName,item.transport?.shippingTypeName)
        holder.hStartPoint.text = item.startPoint!!.getShortName()
        holder.hEndPoint.text = item.endPoint!!.getShortName()

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
                val i = Intent(activity, RouteNewActivity::class.java)
                startActivityForResult(i, ROUTE_NEW_ACTIVITY)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ROUTE_NEW_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                refresh!!.run()
            }
        }
    }

    override fun onItemClick(item: Route) {
        val i = Intent(activity, YRouteDetailActivity::class.java)
        i.put(item)
        startActivity(i)
    }
}
