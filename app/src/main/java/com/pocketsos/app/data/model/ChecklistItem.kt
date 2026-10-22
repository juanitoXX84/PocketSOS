package com.pocketsos.app.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ChecklistItem(
    @SerializedName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerializedName("title")
    val title: String,
    @SerializedName("isChecked")
    val isChecked: Boolean = false,
    @SerializedName("isCustom")
    val isCustom: Boolean = false
)
