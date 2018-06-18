package com.yesat.vozon.ui.courier.transport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import com.yesat.vozon.R
import com.yesat.vozon.models.Transport
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.ui.ListToolbarFragment
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.fragment_list_toolbar.view.*
import kotlinx.android.synthetic.main.item_transport.view.*


class TransportListFragment : ListToolbarFragment<Transport, TransportListFragment.ViewHolder>() {
    companion object {
        const val TRANSPORT_NEW_ACTIVITY = 25
    }

    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        Api.courierService.transports().run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)!!
        v.v_toolbar.title = getString(R.string.your_transports)
        return v
    }


    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transport, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hImage = v.v_image!!
        val hName = v.v_transport!!
        val hNumber= v.v_number!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Transport) {
        holder.hImage.src(item.type?.icon,R.drawable.tmp_truck)
        holder.hImage.addFilter()
        holder.hName.text = item.fullName
        holder.hNumber.text = item.number
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
                val i = Intent(activity, TransportNewActivity::class.java)
                startActivityForResult(i, TRANSPORT_NEW_ACTIVITY)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TRANSPORT_NEW_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                refresh!!.run()
            }
        }
    }

    override fun onItemClick(item: Transport) {
        val i = Intent(activity, TransportDetailActivity::class.java)
        i.put(item)
        startActivityForResult(i,TRANSPORT_NEW_ACTIVITY)
    }
}
