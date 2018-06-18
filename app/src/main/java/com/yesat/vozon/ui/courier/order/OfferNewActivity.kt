package com.yesat.vozon.ui.courier.order

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.yesat.vozon.R
import com.yesat.vozon.models.InfoTmp
import com.yesat.vozon.models.Offer
import com.yesat.vozon.models.Order
import com.yesat.vozon.models.Transport
import com.yesat.vozon.ui.BackPressCompatActivity
import com.yesat.vozon.ui.CodeAdapter
import com.yesat.vozon.ui.courier.transport.TransportListActivity
import com.yesat.vozon.ui.info.InfoTmpActivity
import com.yesat.vozon.utility.*
import com.yesat.vozon.utility.Shared.currencies
import kotlinx.android.synthetic.main.activity_offer_new.*


class OfferNewActivity : BackPressCompatActivity() {
    companion object {
        const val TRANSPORT_REQUEST_CODE = 68
        const val PAYMENT_TYPE_REQUEST_CODE = 86
        const val OTHER_SERVICE_REQUEST_CODE = 57
    }


    private var offer: Offer = Offer()
    private var order: Order = Order()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_new)

        order = intent.get(Order::class.java)
        v_date.text = "Предложение актуально до ${order.shippingDate}"

        v_transport.setOnClickListener{
            val i = Intent(this@OfferNewActivity, TransportListActivity::class.java)
            startActivityForResult(i, TRANSPORT_REQUEST_CODE)
        }
        v_payment_type.setOnClickListener {
            val i = Intent(this@OfferNewActivity,InfoTmpActivity::class.java)
            Shared.call = Api.infoService.paymentType()
            startActivityForResult(i, PAYMENT_TYPE_REQUEST_CODE)
        }
        v_other_service.setOnClickListener {
            val i = Intent(this@OfferNewActivity,InfoTmpActivity::class.java)
            Shared.call = Api.infoService.otherType()
            startActivityForResult(i, OTHER_SERVICE_REQUEST_CODE)
        }

        val currencies = currencies()

        val adapter = CodeAdapter(this, currencies)

        v_spinner.adapter = adapter
        v_spinner.setSelection(0)
        v_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                offer.currency = currencies[position].phoneCode
            }

        }
        v_spinner.onItemSelectedListener
                .onItemSelected(null,null,v_spinner.selectedItemPosition,0)

    }

    private fun create(){
        try{
            offer.price = v_price.get(getString(R.string.enter_price)).toLong()
            checkNotNull(offer.transport){getString(R.string.enter_transport)}
            checkNotNull(offer.paymentType){getString(R.string.enter_payment_type)}
            checkNotNull(offer.otherService){getString(R.string.enter_other_service)}
            offer.haveLoaders = v_have_loaders.isChecked
            offer.comment = v_comment.get(getString(R.string.enter_comment))

            Api.courierService.offerAdd(order.id!!,offer).run2(this,{
                setResult(Activity.RESULT_OK)
                finish()
            },{ _, error ->
                snack(error)
            })

        }catch (ex: IllegalStateException){
            snack(ex.message ?: getString(R.string.something_went_wrong))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                create()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TRANSPORT_REQUEST_CODE -> {
                    offer.transport = data!!.get(Transport::class.java)
                    v_transport.text = offer.transport!!.fullName
                }
                PAYMENT_TYPE_REQUEST_CODE -> {
                    val paymentType =  data!!.get(InfoTmp::class.java)
                    offer.paymentType = paymentType.id
                    v_payment_type.text = paymentType.name!!
                }
                OTHER_SERVICE_REQUEST_CODE -> {
                    val otherService =  data!!.get(InfoTmp::class.java)
                    offer.otherService = otherService.id
                    v_other_service.text = otherService.name!!
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            750 -> if ((grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED))
                finish()
            else ->{}

        }
    }
}









