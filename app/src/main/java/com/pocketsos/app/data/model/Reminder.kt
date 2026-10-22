package com.pocketsos.app.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Reminder(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("title")
    val title: String,
    @SerializedName("isCompleted")
    val isCompleted: Boolean = false,
    @SerializedName("timeTag")
    val timeTag: String? = null,
    @SerializedName("createdAt")
    val createdAt: Long = System.currentTimeMillis()
)
