package com.android.kickwish.Models

data class Wish(
    val sneaker: Sneaker,
    val userId: String,
    val documentId: String? = null
)
