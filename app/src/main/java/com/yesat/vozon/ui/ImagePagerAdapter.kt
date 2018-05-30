package com.yesat.vozon.ui

import android.content.Context
import android.widget.LinearLayout
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v4.view.PagerAdapter
import android.view.View
import com.yesat.vozon.R
import com.yesat.vozon.src
import kotlinx.android.synthetic.main.item_image_pager.view.*


internal class ImagePagerAdapter(
        private val context: Context,
        private val images: List<String?>
    ) : PagerAdapter() {
    private val inflater: LayoutInflater = context.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = inflater.inflate(R.layout.item_image_pager, container, false)

        v.v_image.src = images[position]

        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
        container.removeView(o as LinearLayout)
    }
}