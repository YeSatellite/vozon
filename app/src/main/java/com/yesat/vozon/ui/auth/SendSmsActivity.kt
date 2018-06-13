package com.yesat.vozon.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.yesat.vozon.R
import com.yesat.vozon.models.Country
import com.yesat.vozon.ui.CodeAdapter
import com.yesat.vozon.ui.info.TermsActivity
import com.yesat.vozon.utility.*
import kotlinx.android.synthetic.main.activity_send_sms.*


class SendSmsActivity : AppCompatActivity() {
    var phoneOk = false
    var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_sms)

        authTest()

        v_send_sms.setOnClickListener {
            try {
                if(phoneOk.not()) error("phone not good")
                sendSms(phoneNumber)
            }catch (ex: IllegalStateException){
                snack(ex.message ?: "Unknown error")
            }
        }

        v_sign_up.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }

        var countries: List<Country>
        Api.infoService.countryPhone().run3(this,{
            countries = it
            after(countries)
        })

        v_terms.setOnClickListener{
            startActivity(Intent(this, TermsActivity::class.java))
        }
    }

    private fun after(countries: List<Country>){
        var country: Country
        val adapter = CodeAdapter(this, countries)
        var listener: MaskedTextChangedListener? = null

        v_spinner.adapter = adapter
        v_spinner.setSelection(0)
        v_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                v_phone_number.text.clear()
                country = countries[position]
                v_phone_number.removeTextChangedListener(listener)
                listener = MaskedTextChangedListener(
                        country.phoneMask!!,
                        v_phone_number,
                        object : MaskedTextChangedListener.ValueListener {
                            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                                phoneNumber =  country.phoneCode + extractedValue
                                phoneOk = maskFilled
                            }
                        }
                )
                v_phone_number.addTextChangedListener(listener)
                v_phone_number.onFocusChangeListener = listener
            }

        }
        v_spinner.onItemSelectedListener
                .onItemSelected(null,null,v_spinner.selectedItemPosition,0)
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