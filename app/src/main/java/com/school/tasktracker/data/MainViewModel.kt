package com.school.tasktracker.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.UUID


// ViewModel is going to be used for sharing data across views in a unified way
// And for storing important data in general like Task()

// There will be a lot of example data for now because I am focusing on the UI currently
class MainViewModel: ViewModel() {

    private var _isSelectionView = MutableLiveData(false)
    var isSelectonView = _isSelectionView

    fun toggleSelectionView() {
        _isSelectionView.value = _isSelectionView.value != true
    }

    fun isSelectionViewActive(): Boolean {
        return _isSelectionView.value == true
    }

    private var _tasks = MutableLiveData<List<Task>>(emptyList())
    var tasks: LiveData<List<Task>> = _tasks

    fun isTasksEmpty(): Boolean {
        if (_tasks.value!!.isEmpty()) {
            return true
        } else {
            return false
        }
    }

    fun addTask(task: Task) {
        // Get the current list or an empty list if null, then add the new task
        val updatedList = _tasks.value.orEmpty().toMutableList().apply {
            add(task)
        }
        // Post the updated list to LiveData
        _tasks.value = updatedList
    }

    fun removeTask(task: Task) {
        // Get the current list, filter out the specified task, and post the new list
        val updatedList = _tasks.value.orEmpty().toMutableList().apply {
            remove(task)
        }
        _tasks.value = updatedList
    }

    fun isTask(id: UUID): Boolean {
        return _tasks.value?.any { it.id == id } == true
    }

    fun getTasksByPriority(isPriority: Boolean): List<Task> {
        return _tasks.value
            ?.filter { it.isPriority == isPriority } // Filter tasks based on isPriority
            ?.sortedByDescending { it.isPriority } // Sort by priority if needed
            ?: emptyList() // Return empty list of not found
    }

    var selectedTask: Task? by mutableStateOf(null)

    private var _infoValue = MutableLiveData(List(6) { false })
    val infoValue: LiveData<List<Boolean>> = _infoValue

    private fun updateIndex(index: Int, newValue: Boolean) {
        val tempList = _infoValue.value?.toMutableList()
        tempList?.set(index, newValue)
        _infoValue.value = tempList
    }

    fun getValue(index: Int): Boolean {
        val value = _infoValue.value?.get(index)
        if (value != null && value) {
            return true
        }
        if (value == null) updateIndex(index, false)
        return false
    }

    fun toggleValue(index: Int) {
        var bool = _infoValue.value?.get(index)
        if (bool != null && bool) {
            updateIndex(index, false)
            return
        }
        updateIndex(index, true)
    }

    private val _viewNumber = MutableLiveData(0)
    val viewNumber: LiveData<Int> = _viewNumber

    // For device default color theme
    // So only viable themes are "default", "dark", "light"
    private val _theme = MutableLiveData("default")
    val theme : LiveData<String> = _theme

    fun updateViewNumber(newNumber: Int) {
        _viewNumber.value = newNumber
    }

    fun updateTheme(themeName: String) {
        val themes = arrayOf("default", "dark", "light")

        if (themes.contains(themeName)) {
            _theme.value = themeName
        }
    }
}
