package com.yesat.vozon.models

import com.google.gson.annotations.Expose
import java.io.Serializable

class Category : Serializable {
    @Expose var id: Long? = null
    @Expose var name: String? = null
    @Expose var icon: String? = null
}