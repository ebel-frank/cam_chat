package com.horizons.camchat.model

data class ContactModel(val user_id: String?, val name: String?, val date: String?, val call_type: Int) {
    constructor() : this(null, null, null, -1)
}