package com.yesat.vozon.ui.info


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.yesat.vozon.R
import com.yesat.vozon.utility.get
import com.yesat.vozon.utility.snack
import kotlinx.android.synthetic.main.fragment_location_extra.view.*
import android.view.LayoutInflater



class LocationExtraFragment : Fragment() {

    var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        v =  inflater.inflate(R.layout.fragment_location_extra, container, false)
        return v
    }

    private fun done(){
        try{
            (activity as LocationActivity).i.
                    putExtra(LocationActivity.EXTRA,v!!.v_extra.get("extra is empty"))
            (activity as LocationActivity).next(0)

        }catch (ex: IllegalStateException){
            activity!!.snack(ex.message ?: "Unknown error")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_done, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                done()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
