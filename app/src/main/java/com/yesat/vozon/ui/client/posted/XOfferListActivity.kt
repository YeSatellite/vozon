package com.yesat.vozon.ui.client.posted

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.R
import com.yesat.vozon.ui.ListFragment
import com.yesat.vozon.models.Offer
import com.yesat.vozon.models.Order
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.item_offer.view.*

class XOfferListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_list)
    }
}
class XOfferListFragment : ListFragment<Offer, XOfferListFragment.ViewHolder>() {
    override fun refreshListener(adapter: ListAdapter, srRefresh: SwipeRefreshLayout) {
        val order = activity!!.intent.get(Order::class.java)
        Api.clientService.offers(order.id!!).run2(srRefresh,{ body ->
            adapter.list = body
            adapter.notifyDataSetChanged()
        },{ _, error ->
            activity?.snack(error)
        })
    }

    override fun onCreateViewHolder2(parent: ViewGroup): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_offer, parent, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(v: View) : ListFragment.ViewHolder(v){
        val hAvatar = v.v_avatar!!
        val hName = v.v_name!!
        val hRating = v.v_rating!!
        val hDate = v.v_date!!
        val hAbout = v.v_about!!
        val hTName = v.v_t_name!!
        val hTType = v.v_name!!
        val hPrice = v.v_price!!
        val hAccept = v.v_accept!!
        val hReject = v.v_reject!!

    }
    override fun onBindViewHolder2(holder: ViewHolder, item: Offer) {
        holder.hAvatar.src = item.transport?.owner?.avatar
        holder.hName.text = item.transport?.owner?.name
        holder.hRating.text = item.transport?.owner?.rating
        // TODO hDate
        holder.hAbout.text = item.transport?.owner?.about
        holder.hTName.text = item.transport?.modelName
        // TODO hTType
        holder.hPrice.text = "${item.price} тг"

        holder.hAccept.setOnClickListener {
            Api.clientService.offersAccept(item.order!!, item.id!!)
                    .run2(activity!!,{
                        activity!!.setResult(Activity.RESULT_OK, Intent())
                        activity!!.finish()
                    },{
                        _, error ->
                        activity!!.snack(error)
                        norm(item.order!!.toString() + " "+item.id!!)
                    })
        }
        holder.hReject.setOnClickListener{
            //TODO
        }

    }
}