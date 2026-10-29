package com.example.mallnav.data.model

data class Store(
    val id: String,
    val mallId: String,
    val name: String,
    val category: String,
    val floor: String,
    val unit: String,
    val description: String,
    val isLandmark: Boolean = false,
)
