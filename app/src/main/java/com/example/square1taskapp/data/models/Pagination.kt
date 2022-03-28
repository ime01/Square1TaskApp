package com.example.square1taskapp.data.models

data class Pagination(
    val current_page: Int?,
    val last_page: Int?,
    val per_page: Int?,
    val total: Int?
)