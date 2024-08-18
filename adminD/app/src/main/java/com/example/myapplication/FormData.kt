package com.example.admin

import java.io.Serializable

data class FormData(
    val key: String = "",
    val name: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val itemName: String = "",
    val itemQuantity: Int = 0
) : Serializable
