package com.yesat.vozon.ui.courier.order


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.R
import kotlinx.android.synthetic.main.fragment_order_list.view.*


class YOrderPList0Fragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.fragment_order_list, container, false)
        v.v_city_add.setOnClickListener{
            val i = Intent(activity, OrderFilterActivity::class.java)
            startActivity(i)
        }
        return v
    }

}
