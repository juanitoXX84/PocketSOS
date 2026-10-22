package com.pocketsos.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pocketsos.app.data.model.ChecklistItem
import com.pocketsos.app.data.model.CustomList
import com.pocketsos.app.data.model.HistoryEntry
import com.pocketsos.app.data.model.Reminder

class PocketSosPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pocket_sos_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_EXIT_MODE_ITEMS = "key_exit_mode_items"
        private const val KEY_CUSTOM_LISTS = "key_custom_lists"
        private const val KEY_REMINDERS = "key_reminders"
        private const val KEY_HISTORY = "key_history"
        private const val KEY_FIRST_RUN = "key_first_run"

        val DEFAULT_EXIT_ITEMS = listOf(
            ChecklistItem(title = "Llaves", isChecked = false),
            ChecklistItem(title = "Cartera", isChecked = false),
            ChecklistItem(title = "Celular", isChecked = false),
            ChecklistItem(title = "Audífonos", isChecked = false),
            ChecklistItem(title = "Identificación", isChecked = false),
            ChecklistItem(title = "Cargador", isChecked = false)
        )

        val DEFAULT_CUSTOM_LISTS = listOf(
            CustomList(
                title = "Universidad",
                items = listOf(
                    ChecklistItem(title = "Mochila"),
                    ChecklistItem(title = "Cuaderno"),
                    ChecklistItem(title = "Laptop"),
                    ChecklistItem(title = "Credencial"),
                    ChecklistItem(title = "Botella de agua")
                )
            ),
            CustomList(
                title = "Trabajo",
                items = listOf(
                    ChecklistItem(title = "Laptop"),
                    ChecklistItem(title = "Credencial corporativa"),
                    ChecklistItem(title = "Cargador"),
                    ChecklistItem(title = "Agenda"),
                    ChecklistItem(title = "Lentes")
                )
            ),
            CustomList(
                title = "Viaje",
                items = listOf(
                    ChecklistItem(title = "Pasaporte"),
                    ChecklistItem(title = "Ropa"),
                    ChecklistItem(title = "Cepillo de dientes"),
                    ChecklistItem(title = "Boletos"),
                    ChecklistItem(title = "Cargador portátil")
                )
            )
        )
    }

    init {
        val isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true)
        if (isFirstRun) {
            saveExitModeItems(DEFAULT_EXIT_ITEMS)
            saveCustomLists(DEFAULT_CUSTOM_LISTS)
            saveReminders(
                listOf(
                    Reminder(title = "Cerrar las ventanas", timeTag = "Antes de salir"),
                    Reminder(title = "Apagar las luces", timeTag = "Antes de salir")
                )
            )
            saveHistory(emptyList())
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply()
        }
    }

    // --- Modo Salida Items ---
    fun getExitModeItems(): List<ChecklistItem> {
        val json = prefs.getString(KEY_EXIT_MODE_ITEMS, null) ?: return DEFAULT_EXIT_ITEMS
        return try {
            val type = object : TypeToken<List<ChecklistItem>>() {}.type
            gson.fromJson(json, type) ?: DEFAULT_EXIT_ITEMS
        } catch (e: Exception) {
            DEFAULT_EXIT_ITEMS
        }
    }

    fun saveExitModeItems(items: List<ChecklistItem>) {
        val json = gson.toJson(items)
        prefs.edit().putString(KEY_EXIT_MODE_ITEMS, json).apply()
    }

    // --- Custom Lists ---
    fun getCustomLists(): List<CustomList> {
        val json = prefs.getString(KEY_CUSTOM_LISTS, null) ?: return DEFAULT_CUSTOM_LISTS
        return try {
            val type = object : TypeToken<List<CustomList>>() {}.type
            gson.fromJson(json, type) ?: DEFAULT_CUSTOM_LISTS
        } catch (e: Exception) {
            DEFAULT_CUSTOM_LISTS
        }
    }

    fun saveCustomLists(lists: List<CustomList>) {
        val json = gson.toJson(lists)
        prefs.edit().putString(KEY_CUSTOM_LISTS, json).apply()
    }

    // --- Reminders ---
    fun getReminders(): List<Reminder> {
        val json = prefs.getString(KEY_REMINDERS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<Reminder>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveReminders(reminders: List<Reminder>) {
        val json = gson.toJson(reminders)
        prefs.edit().putString(KEY_REMINDERS, json).apply()
    }

    // --- History ---
    fun getHistory(): List<HistoryEntry> {
        val json = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<HistoryEntry>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveHistory(history: List<HistoryEntry>) {
        val json = gson.toJson(history)
        prefs.edit().putString(KEY_HISTORY, json).apply()
    }
}
