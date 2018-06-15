package com.yesat.vozon.ui.client


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
import com.yesat.vozon.utility.Shared
import kotlinx.android.synthetic.main.tmp_pager.view.*

class XOrderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.tmp_pager, container, false)
        v.pager.adapter = OrderPagesAdapter(childFragmentManager)
        val tabs = v.tab_layout
        tabs.shouldExpand = true
        tabs.setViewPager(v.pager)

        v.v_toolbar.title = getString(R.string.orders)
        (activity as AppCompatActivity).setSupportActionBar(v.v_toolbar)
        return v
    }


    inner class OrderPagesAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private var tabs = arrayOf("Новые", "Активные")

        override fun getPageTitle(position: Int) = tabs[position]
        override fun getCount() = tabs.size

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> XOrderPListFragment()
                1 -> XOrderAListFragment()
                else -> null
            }
        }
    }


}
