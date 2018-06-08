package com.yesat.vozon.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.yesat.vozon.utility.Shared
import com.yesat.vozon.utility.norm


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        Shared.token = FirebaseInstanceId.getInstance().token ?: ""
        norm(Shared.token)
    }
}
