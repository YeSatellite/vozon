package com.yesat.vozon.ui.courier.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yesat.vozon.R
import com.yesat.vozon.utility.Shared
import kotlinx.android.synthetic.main.tmp_pager.view.*


class YOrderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.tmp_pager, container, false)

        val typedValue = TypedValue()
        context!!.theme.resolveAttribute(R.attr.textColorLarge, typedValue, true)
        val color = typedValue.data
        v.v_toolbar.setTitleTextColor(color)
        v.v_toolbar.setSubtitleTextColor(color)

        v.pager.adapter = OrderPagesAdapter(childFragmentManager)
        val tabs = v.tab_layout
        tabs.shouldExpand = true
        tabs.setViewPager(v.pager)

        (activity as AppCompatActivity).setSupportActionBar(v.v_toolbar)

        v.pager.currentItem = when(activity!!.intent.getStringExtra(Shared.action)){
            Shared.Action.acceptOffer,
            Shared.Action.done -> {
                activity!!.intent.removeExtra(Shared.action)
                2
            }
            else -> 0
        }


        return v
    }


    inner class OrderPagesAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private var tabs = arrayOf("Posted", "Waiting", "Active")

        override fun getPageTitle(position: Int) = tabs[position]
        override fun getCount() = tabs.size

        override fun getItem(position: Int): Fragment? {
            return when (position) {
                0 -> YOrderPListFragment()
                1 -> YOrderWListFragment()
                2 -> YOrderAListFragment()
                else -> null
            }
        }
    }


}
