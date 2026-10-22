package com.pocketsos.app.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pocketsos.app.data.local.PocketSosPreferences
import com.pocketsos.app.data.model.ChecklistItem
import com.pocketsos.app.data.model.CustomList
import com.pocketsos.app.data.model.HistoryEntry
import com.pocketsos.app.data.model.Reminder
import com.pocketsos.app.data.repository.PocketSosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PocketSosViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = PocketSosPreferences(application)
    private val repository = PocketSosRepository(preferences)

    val exitModeItems: StateFlow<List<ChecklistItem>> = repository.exitModeItems
    val customLists: StateFlow<List<CustomList>> = repository.customLists
    val reminders: StateFlow<List<Reminder>> = repository.reminders
    val history: StateFlow<List<HistoryEntry>> = repository.history

    val exitProgress: StateFlow<Pair<Int, Int>> = exitModeItems.map { items ->
        val completed = items.count { it.isChecked }
        val total = items.size
        Pair(completed, total)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Pair(0, 0))

    val isExitModeReady: StateFlow<Boolean> = exitModeItems.map { items ->
        items.isNotEmpty() && items.all { it.isChecked }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // --- Modo Salida ---
    fun toggleExitItem(id: String) {
        repository.toggleExitItem(id)
    }

    fun addExitItem(title: String) {
        repository.addExitItem(title)
    }

    fun deleteExitItem(id: String) {
        repository.deleteExitItem(id)
    }

    fun resetExitMode() {
        repository.resetExitMode()
    }

    fun resetExitModeToDefaults() {
        repository.resetToDefaults()
    }

    fun finishExitSession(listName: String = "Modo salida"): HistoryEntry? {
        return repository.finishExitSession(listName)
    }

    // --- Custom Lists ---
    fun getListById(listId: String): CustomList? {
        return customLists.value.find { it.id == listId }
    }

    fun createCustomList(title: String, initialItems: List<String> = emptyList()) {
        repository.createCustomList(title, initialItems)
    }

    fun updateCustomListTitle(listId: String, newTitle: String) {
        repository.updateCustomListTitle(listId, newTitle)
    }

    fun deleteCustomList(listId: String) {
        repository.deleteCustomList(listId)
    }

    fun toggleCustomListItem(listId: String, itemId: String) {
        repository.toggleCustomListItem(listId, itemId)
    }

    fun addCustomListItem(listId: String, title: String) {
        repository.addCustomListItem(listId, title)
    }

    fun deleteCustomListItem(listId: String, itemId: String) {
        repository.deleteCustomListItem(listId, itemId)
    }

    fun resetCustomList(listId: String) {
        repository.resetCustomList(listId)
    }

    fun loadCustomListIntoExitMode(listId: String) {
        repository.loadListIntoExitMode(listId)
    }

    // --- Recordatorios ---
    fun toggleReminder(id: String) {
        repository.toggleReminder(id)
    }

    fun addReminder(title: String, timeTag: String? = null) {
        repository.addReminder(title, timeTag)
    }

    fun deleteReminder(id: String) {
        repository.deleteReminder(id)
    }

    // --- Historial ---
    fun clearHistory() {
        repository.clearHistory()
    }
}
