package com.pocketsos.app.data.repository

import com.pocketsos.app.data.local.PocketSosPreferences
import com.pocketsos.app.data.model.ChecklistItem
import com.pocketsos.app.data.model.CustomList
import com.pocketsos.app.data.model.HistoryEntry
import com.pocketsos.app.data.model.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PocketSosRepository(private val preferences: PocketSosPreferences) {

    private val _exitModeItems = MutableStateFlow<List<ChecklistItem>>(preferences.getExitModeItems())
    val exitModeItems: StateFlow<List<ChecklistItem>> = _exitModeItems.asStateFlow()

    private val _customLists = MutableStateFlow<List<CustomList>>(preferences.getCustomLists())
    val customLists: StateFlow<List<CustomList>> = _customLists.asStateFlow()

    private val _reminders = MutableStateFlow<List<Reminder>>(preferences.getReminders())
    val reminders: StateFlow<List<Reminder>> = _reminders.asStateFlow()

    private val _history = MutableStateFlow<List<HistoryEntry>>(preferences.getHistory())
    val history: StateFlow<List<HistoryEntry>> = _history.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())

    // ===========================
    // Modo Salida
    // ===========================

    fun toggleExitItem(id: String) {
        val updated = _exitModeItems.value.map { item ->
            if (item.id == id) item.copy(isChecked = !item.isChecked) else item
        }
        _exitModeItems.value = updated
        preferences.saveExitModeItems(updated)
    }

    fun addExitItem(title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        val newItem = ChecklistItem(title = trimmed, isChecked = false, isCustom = true)
        val updated = _exitModeItems.value + newItem
        _exitModeItems.value = updated
        preferences.saveExitModeItems(updated)
    }

    fun deleteExitItem(id: String) {
        val updated = _exitModeItems.value.filter { it.id != id }
        _exitModeItems.value = updated
        preferences.saveExitModeItems(updated)
    }

    fun resetExitMode() {
        val updated = _exitModeItems.value.map { it.copy(isChecked = false) }
        _exitModeItems.value = updated
        preferences.saveExitModeItems(updated)
    }

    fun resetToDefaults() {
        val updated = PocketSosPreferences.DEFAULT_EXIT_ITEMS
        _exitModeItems.value = updated
        preferences.saveExitModeItems(updated)
    }

    fun finishExitSession(listName: String = "Modo salida"): HistoryEntry? {
        val items = _exitModeItems.value
        if (items.isEmpty()) return null

        val completed = items.count { it.isChecked }
        val total = items.size
        val now = System.currentTimeMillis()
        val formatted = dateFormatter.format(Date(now))

        val entry = HistoryEntry(
            listName = listName,
            completedCount = completed,
            totalCount = total,
            timestamp = now,
            formattedDate = formatted
        )

        // Prepend new entry
        val updatedHistory = listOf(entry) + _history.value
        _history.value = updatedHistory
        preferences.saveHistory(updatedHistory)

        // Reset checklist for a new exit
        resetExitMode()
        return entry
    }

    // ===========================
    // Mis Listas
    // ===========================

    fun createCustomList(title: String, initialItemNames: List<String> = emptyList()) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        val items = initialItemNames.filter { it.isNotBlank() }.map {
            ChecklistItem(title = it.trim(), isChecked = false, isCustom = false)
        }
        val newList = CustomList(title = trimmed, items = items)
        val updated = _customLists.value + newList
        _customLists.value = updated
        preferences.saveCustomLists(updated)
    }

    fun updateCustomListTitle(listId: String, newTitle: String) {
        val trimmed = newTitle.trim()
        if (trimmed.isEmpty()) return
        val updated = _customLists.value.map { list ->
            if (list.id == listId) list.copy(title = trimmed) else list
        }
        _customLists.value = updated
        preferences.saveCustomLists(updated)
    }

    fun deleteCustomList(listId: String) {
        val updated = _customLists.value.filter { it.id != listId }
        _customLists.value = updated
        preferences.saveCustomLists(updated)
    }

    fun toggleCustomListItem(listId: String, itemId: String) {
        val updated = _customLists.value.map { list ->
            if (list.id == listId) {
                val updatedItems = list.items.map { item ->
                    if (item.id == itemId) item.copy(isChecked = !item.isChecked) else item
                }
                list.copy(items = updatedItems)
            } else list
        }
        _customLists.value = updated
        preferences.saveCustomLists(updated)
    }

    fun addCustomListItem(listId: String, title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        val newItem = ChecklistItem(title = trimmed, isChecked = false, isCustom = true)
        val updated = _customLists.value.map { list ->
            if (list.id == listId) {
                list.copy(items = list.items + newItem)
            } else list
        }
        _customLists.value = updated
        preferences.saveCustomLists(updated)
    }

    fun deleteCustomListItem(listId: String, itemId: String) {
        val updated = _customLists.value.map { list ->
            if (list.id == listId) {
                list.copy(items = list.items.filter { it.id != itemId })
            } else list
        }
        _customLists.value = updated
        preferences.saveCustomLists(updated)
    }

    fun resetCustomList(listId: String) {
        val updated = _customLists.value.map { list ->
            if (list.id == listId) {
                list.copy(items = list.items.map { it.copy(isChecked = false) })
            } else list
        }
        _customLists.value = updated
        preferences.saveCustomLists(updated)
    }

    fun loadListIntoExitMode(listId: String) {
        val target = _customLists.value.find { it.id == listId } ?: return
        val newExitItems = target.items.map { it.copy(isChecked = false) }
        _exitModeItems.value = newExitItems
        preferences.saveExitModeItems(newExitItems)
    }

    // ===========================
    // Recordatorios
    // ===========================

    fun toggleReminder(id: String) {
        val updated = _reminders.value.map { rem ->
            if (rem.id == id) rem.copy(isCompleted = !rem.isCompleted) else rem
        }
        _reminders.value = updated
        preferences.saveReminders(updated)
    }

    fun addReminder(title: String, timeTag: String? = null) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        val newReminder = Reminder(
            title = trimmed,
            isCompleted = false,
            timeTag = timeTag?.trim()?.ifEmpty { null }
        )
        val updated = _reminders.value + newReminder
        _reminders.value = updated
        preferences.saveReminders(updated)
    }

    fun deleteReminder(id: String) {
        val updated = _reminders.value.filter { it.id != id }
        _reminders.value = updated
        preferences.saveReminders(updated)
    }

    // ===========================
    // Historial
    // ===========================

    fun clearHistory() {
        _history.value = emptyList()
        preferences.saveHistory(emptyList())
    }
}
