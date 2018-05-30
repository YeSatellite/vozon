package com.yesat.vozon.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Offer : Serializable {
    @Expose var id: Long? = null

    @Expose var transport: Transport? = null
        set(value) {
            field = value
            transportId = value!!.id
        }
    @SerializedName("transport_id")
    @Expose var transportId: Long? = null

    @Expose var order: Long? = null

    @Expose var price: Long? = null

    @SerializedName("payment_type")
    @Expose var paymentType: Long? = null
    @SerializedName("payment_type_name")
    @Expose var paymentTypeName: String? = null

    @SerializedName("other_service")
    @Expose var otherService: Long? = null
    @SerializedName("other_service_name")
    @Expose var otherServiceName: String? = null

    @SerializedName("shipping_type")
    @Expose var shippingType: Long? = null
    @SerializedName("shipping_type_name")
    @Expose var shippingTypeName: String? = null

    @Expose var comment: String? = null
    override fun toString(): String {
        return """
            |Offer(
            |   id=$id,
            |   transport=$transport,
            |   transportId=$transportId,
            |   order=$order, price=$price,
            |   paymentType=$paymentType,
            |   paymentTypeName=$paymentTypeName,
            |   otherService=$otherService,
            |   otherServiceName=$otherServiceName,
            |   shippingType=$shippingType,
            |   shippingTypeName=$shippingTypeName,
            |   comment=$comment
            |)""".trimMargin()
    }


}
