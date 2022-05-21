package com.example.projectcloud.model

data class chatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}
