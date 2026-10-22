package com.pocketsos.app.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CustomList(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("title")
    val title: String,
    @SerializedName("items")
    val items: List<ChecklistItem> = emptyList(),
    @SerializedName("createdAt")
    val createdAt: Long = System.currentTimeMillis()
) {
    val completedCount: Int get() = items.count { it.isChecked }
    val totalCount: Int get() = items.size
    val progress: Float get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount
    val isAllChecked: Boolean get() = totalCount > 0 && completedCount == totalCount
}
