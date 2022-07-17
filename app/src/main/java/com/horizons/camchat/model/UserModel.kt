package com.horizons.camchat.model

data class UserModel(var user_id: String?, val name: String?) {
    constructor() : this(null, null)
}