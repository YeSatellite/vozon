package com.yesat.vozon.ui.courier.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.R
import com.yesat.vozon.ui.client.active.XOrderAListFragment
import com.yesat.vozon.ui.client.posted.XOrderPListFragment
import kotlinx.android.synthetic.main.tmp_pager.view.*

class YOrderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.tmp_pager, container, false)

        v.pager.adapter = OrderPagesAdapter(childFragmentManager)
        val tabs = v.tab_layout
        tabs.shouldExpand = true
        tabs.setViewPager(v.pager)

        (activity as AppCompatActivity).setSupportActionBar(v.toolbar)

        return v
    }


    inner class OrderPagesAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private var tabs = arrayOf("Posted", "Waiting", "Active")

        override fun getPageTitle(position: Int) = tabs[position]
        override fun getCount() = tabs.size

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> YOrderPListFragment()
                1 -> YOrderPListFragment()
                2 -> YOrderPListFragment()
                else -> null
            }
        }
    }


}
