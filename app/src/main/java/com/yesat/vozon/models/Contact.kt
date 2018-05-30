package com.yesat.vozon.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Contact {
    @SerializedName("name")
    @Expose var firstName: String? = null
    @SerializedName("username")
    @Expose var lastName: String? = null
    @SerializedName("phone")
    @Expose var phone: String? = null

    constructor(firstName: String?, lastName: String?, phone: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phone
    }
}