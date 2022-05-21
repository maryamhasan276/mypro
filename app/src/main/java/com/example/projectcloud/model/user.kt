package com.example.projectcloud.model

data class user (
    var username: String = "",
    var email: String = "",
    val user_image: String?,
    val user_type: String?,
    val registrationTokens: MutableList<String>
) {
    constructor() : this("", "", null,"", mutableListOf())
}

