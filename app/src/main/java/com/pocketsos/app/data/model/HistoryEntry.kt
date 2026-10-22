package com.pocketsos.app.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class HistoryEntry(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("listName")
    val listName: String,
    @SerializedName("completedCount")
    val completedCount: Int,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    @SerializedName("formattedDate")
    val formattedDate: String
)
