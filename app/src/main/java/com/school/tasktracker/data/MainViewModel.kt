package com.school.tasktracker.data

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID

// ViewModel is going to be used for sharing data across views in a unified way
// And for storing important data in general like Task()

// There will be a lot of example data for now because I am focusing on the UI currently

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = application.applicationContext
    private var _tasks = MutableLiveData<List<Task>>(emptyList())
    val tasks: LiveData<List<Task>> = _tasks

    private val gson = Gson()
    private val fileName = "tasks.json"

    private val _triggerImport = MutableLiveData<Boolean>()
    val triggerImport: LiveData<Boolean> = _triggerImport

    private val _triggerExport = MutableLiveData<Boolean>()
    val triggerExport: LiveData<Boolean> = _triggerExport

    fun requestImport() {
        _triggerImport.value = true
        _triggerImport.value = false
    }

    fun requestExport() {
        _triggerExport.value = true
        _triggerExport.value = false
    }

    // Import tasks from JSON
    fun importTasks(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Open the selected file
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = InputStreamReader(inputStream)

                // Deserialize the JSON to a list of tasks
                val importedTasks: List<Task> =
                    gson.fromJson(reader, Array<Task>::class.java).toList()

                // Validate the tasks and replace current tasks
                if (importedTasks.isNotEmpty() && validateTasks(importedTasks)) {
                    _tasks.postValue(importedTasks)
                    println("Tasks imported successfully.")
                } else {
                    println("Invalid task data in the selected file.")
                }

            } catch (e: Exception) {
                println("Error importing tasks: ${e.message}")
            }
        }
    }

    // Export tasks to JSON
    fun exportTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Serialize current tasks to JSON
                val json = gson.toJson(_tasks.value.orEmpty())

                // Create a new file in the app's private storage
                val file = File(context.filesDir, fileName)
                FileOutputStream(file).apply {
                    write(json.toByteArray())
                    close()
                }

                println("Tasks exported successfully.")
            } catch (e: Exception) {
                println("Error exporting tasks: ${e.message}")
            }
        }
    }

    // Validate task data (ensure all properties are correctly formatted)
    private fun validateTasks(tasks: List<Task>): Boolean {
        // Add validation logic (e.g., checking if all properties are not null)
        return tasks.all {
            it.title.isNotBlank() && it.description.isNotBlank() && it.date.isNotBlank()
        }
    }

    // Helper function to write tasks to the provided URI (for export)
    fun saveTasksToUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val json = gson.toJson(_tasks.value.orEmpty())
                val outputStream = context.contentResolver.openOutputStream(uri)
                outputStream?.write(json.toByteArray())
                outputStream?.close()
                println("Tasks exported successfully.")
            } catch (e: Exception) {
                println("Error saving tasks to URI: ${e.message}")
            }
        }
    }

    // Initialize the file with an empty list if it doesn't exist
    fun initializeTasksFile() {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            file.writeText("[]")  // Create an empty JSON array if the file doesn't exist
        }
    }

    // Save tasks to file
    fun saveTasksToFile() {
        try {
            val file = File(context.filesDir, fileName)
            val json = gson.toJson(_tasks.value.orEmpty()) // Serialize list to JSON
            file.writeText(json) // Write JSON to file
        } catch (e: Exception) {
            Log.e("TaskFile", "Error saving tasks to file: ${e.message}")
        }
    }

    // Load tasks from file asynchronously
    fun loadTasksFromFile() {
        // Load tasks on a background thread to avoid blocking UI thread
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)
                if (file.exists()) {
                    val json = file.readText() // Read JSON from file
                    val loadedTasks = gson.fromJson(json, Array<Task>::class.java)
                        .toList() // Deserialize JSON to list of Task
                    // Update LiveData on the main thread after loading
                    _tasks.postValue(loadedTasks) // Use postValue to update LiveData from background thread
                }
            } catch (e: Exception) {
                Log.e("TaskFile", "Error loading tasks from file: ${e.message}")
            }
        }
    }


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

    fun removeTaskById(taskId: UUID) {
        // Get the current list, filter out the task with the matching id, and update the LiveData
        val updatedList = _tasks.value.orEmpty().filter { it.id != taskId }
        _tasks.value = updatedList
    }

    fun isTask(id: UUID): Boolean {
        return _tasks.value?.any { it.id == id } == true
    }

    fun updateTaskById(
        taskId: UUID,
        title: String,
        description: String,
        isPriority: Boolean,
        date: String,
        time: String
    ) {
        // Create a copy of the current task list
        val updatedTasks = _tasks.value.orEmpty().toMutableList()

        // Find the index of the task in the list
        val index = updatedTasks.indexOfFirst { it.id == taskId }

        if (index != -1) {
            // Update the task in place
            updatedTasks[index] = updatedTasks[index].copy(
                title = title,
                description = description,
                isPriority = isPriority,
                date = date,
                time = time
            )

            _tasks.value = updatedTasks
        }
    }

    private var _selectedTasks = MutableLiveData<List<Task>>(emptyList())
    var selectedTasks: LiveData<List<Task>> = _selectedTasks

    fun isSelectedTasksEmpty(): Boolean {
        if (_selectedTasks.value!!.isEmpty()) {
            return true
        } else {
            return false
        }
    }

    fun deleteSelectedTasks() {

    }

    fun deleteTasks() {
        val selected = _selectedTasks.value.orEmpty()
        val updatedTasks = _tasks.value.orEmpty().toMutableList()

        // Remove each selected task from the main task list
        selected.forEach { selectedTask ->
            updatedTasks.removeAll { it.id == selectedTask.id }
        }

        // Update the main task list
        _tasks.value = updatedTasks

        // Clear the selected tasks list
        _selectedTasks.value = emptyList()
    }

    fun isSelectedTasksFull(): Boolean {
        val selected = _selectedTasks.value.orEmpty()
        val tasks = _tasks.value.orEmpty()

        return when {
            selected.isEmpty() -> false // No selected tasks
            selected.size == tasks.size -> true // All tasks are selected
            else -> false // Partial selection
        }
    }

    fun addSelectedTask(task: Task) {
        // Get the current list or an empty list if null, then add the new task
        val updatedList = _selectedTasks.value.orEmpty().toMutableList().apply {
            add(task)
        }
        // Post the updated list to LiveData
        _selectedTasks.value = updatedList
    }

    fun removeSelectedTask(task: Task) {
        // Get the current list or an empty list if null, then remove the specified task
        val updatedList = _selectedTasks.value.orEmpty().toMutableList().apply {
            remove(task)
        }
        // Post the updated list to LiveData
        _selectedTasks.value = updatedList
    }

    private val _isSelectionMode = MutableLiveData(false)
    val isSelectionMode: LiveData<Boolean> = _isSelectionMode

    fun toggleSelectionMode() {
        if (_isSelectionMode.value == false) {
            _isSelectionMode.value = true
        } else {
            _isSelectionMode.value = false
        }
    }

    fun selectAllTasks() {
        _selectedTasks.value = _tasks.value.orEmpty().toList()
    }

    fun deSelectAllTasks() {
        _selectedTasks.value = emptyList()
    }

    var selectedTask: Task? by mutableStateOf(null)

    fun sizeOfSelectedTasks(): Int {
        return _selectedTasks.value?.size ?: 0
    }

    fun isTaskSelected(taskId: UUID): Boolean {
        // Check if selectedTasks contains a task with the given ID
        return _selectedTasks.value?.any { it.id == taskId } == true
    }

    private val _isTaskSelectedInSelectionMode = MutableLiveData(false)
    val isTaskSelectedInSelectionMode: LiveData<Boolean> = _isTaskSelectedInSelectionMode

    fun toggleTaskSelectedInSelectionMode() {
        if (_isTaskSelectedInSelectionMode.value == false) {
            _isTaskSelectedInSelectionMode.value = true
        } else {
            _isTaskSelectedInSelectionMode.value = false
        }
    }

    fun resetSelectionState() {
        _selectedTasks.value = emptyList()
        _isSelectionMode.value = false
        _isTaskSelectedInSelectionMode.value = false
        _isSelectionViewActive.value = false
    }

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


    private val _isSelectionViewActive = MutableLiveData(false)
    val isSelectionViewActive: LiveData<Boolean> = _isSelectionViewActive

    fun toggleSelectionView() {
        var bool = isSelectionViewActive.value
        if (bool == true) {
            _isSelectionViewActive.value = false
            return
        }
        _isSelectionViewActive.value = true
    }

    private val _hideEditButton = MutableLiveData(false)
    val hideEditButton: LiveData<Boolean> = _hideEditButton

    fun changeEditButton(bool: Boolean) {
        _hideEditButton.value = bool
    }

    private val _viewNumber = MutableLiveData(0)
    val viewNumber: LiveData<Int> = _viewNumber

    fun updateViewNumber(newNumber: Int) {
        _viewNumber.value = newNumber
    }

    // For device default color theme
    // So only viable themes are "default", "dark", "light"
    private val _theme = MutableLiveData("default")
    val theme: LiveData<String> = _theme

    private val _themeBool = MutableLiveData<Boolean>()
    val themeBool: LiveData<Boolean> = _themeBool


    fun updateTheme(themeName: String) {
        val themes = arrayOf("default", "dark", "light")
        if (themes.contains(themeName)) {
            _theme.value = themeName
            updateThemeBool()
        }
    }

    fun updateThemeBool() {
        _themeBool.value = when (_theme.value) {
            "dark" -> true
            "light" -> false
            else -> isSystemInDarkMode()
        }
    }

    private fun isSystemInDarkMode(): Boolean {
        val currentNightMode = getApplication<Application>().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }


    // To return black or white for view themes
    fun getColor(): Color {
        return if (_themeBool.value == true) {
            Color.White
        } else {
            Color.Black
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDays(dueDate: String): Int {
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val current = LocalDate.now() // Current date
        val due = LocalDate.parse(dueDate, formatter) // Parse due date

        return ChronoUnit.DAYS.between(current, due).toInt()
    }
}

class MainViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
