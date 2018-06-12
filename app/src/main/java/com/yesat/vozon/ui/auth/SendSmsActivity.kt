package com.yesat.vozon.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.yesat.vozon.R
import com.yesat.vozon.models.Country
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_send_sms.*


class SendSmsActivity : AppCompatActivity() {

    var countries: List<Country>? = null
    var country: Country? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_sms)

        authTest()

        Api.infoService.countryPhone().run3(this,{
            countries = it
            country = it.first{ it.id == 3L}
            after()
        })


    }

    private fun after(){
        val adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                countries!!.map { country -> country.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        val listener = MaskedTextChangedListener(
                country?.phoneMask!!,
                v_phone_number,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                        norm(extractedValue)
                        norm(maskFilled.toString())
                    }
                }
        )

        v_phone_number.addTextChangedListener(listener)
        v_phone_number.onFocusChangeListener = listener
        v_phone_number.hint = listener.placeholder()

        v_send_sms.setOnClickListener {
            try {
                val phone = v_phone_number.get("phone is empty")
                sendSms(phone)
            }catch (ex: IllegalStateException){
                snack(ex.message ?: "Unknown error")
            }
        }

        v_sign_up.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }
    }

    private fun sendSms(phone: String){
        val token = Shared.token
        if (token.isEmpty()){
            snack("Try later")
        }
        val map = hashMapOf("phone" to phone)
        Api.authService.sentSms(map).run2(this,{
            val i = Intent(this, LoginActivity::class.java)
            i.put(phone)
            startActivity(i)
        },{ _, error ->
            snack(error)
        })
    }


    private fun authTest() {
        if (Shared.currentUser.token != null){
            val status = clientOrCourier()
            val i = Intent(this, status)
            val action = intent.getStringExtra(Shared.action)
            norm(action)
            i.putExtra(Shared.action,action)
            startActivityForResult(i,0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Shared.currentUser.token != null)
            finish()
    }
}
