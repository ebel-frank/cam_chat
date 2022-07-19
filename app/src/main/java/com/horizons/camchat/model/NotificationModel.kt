package com.horizons.camchat.model

data class NotificationModel(var id: String?, val message: String?, val date: String?, val user_id: String?) {
    constructor() : this(null, null, null, null)
}