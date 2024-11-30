package com.school.tasktracker.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.UUID
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

// ViewModel is going to be used for sharing data across views in a unified way
// And for storing important data in general like Task()

// There will be a lot of example data for now because I am focusing on the UI currently
class MainViewModel: ViewModel() {

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

    fun removeTaskById(taskId: UUID) {
        // Get the current list, filter out the task with the matching id, and update the LiveData
        val updatedList = _tasks.value.orEmpty().filter { it.id != taskId }
        _tasks.value = updatedList
    }

    fun isTask(id: UUID): Boolean {
        return _tasks.value?.any { it.id == id } == true
    }

    // Function that uses kotlins receiver feature
    // https://stackoverflow.com/questions/45875491/what-is-a-receiver-in-kotlin
    // This serves as a easy function for getting task by id and editing its properties

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

            // Update the LiveData value
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
        return  _selectedTasks.value?.size ?: 0
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

    //call this before closing app
    fun saveTasks(filepath: String) {

        val path = context.getFilesDir()

        //using Gson because the task UUID has issues with implementing serializable
        val gson = Gson()
        //declare a file object with the filepath
        val file = File(filepath)
        //load any pre existing json tasks at the filepath to _tasks
        loadTasks(filepath)
        //instance of updated list in viewmodel list with additional check for unique ids
        val updatedList = _tasks.value.orEmpty().distinctBy { it.id }
        //since loadtasks() has been called and updated the view model already, we can clear the current saved data
        file.writeText("")

        // Convert the updated task list to JSON
        val updatedJson = gson.toJson(updatedList)

        // Write the updated JSON back to the file
        file.writeText(updatedJson)

    }

    //call this on app start up
    fun loadTasks(filepath: String) {
        val updatedList = _tasks.value.orEmpty().toMutableList()
        val gson = Gson()
        val existingTasks: MutableList<Task> = mutableListOf()
        val file = File(filepath)
        //check if path exists
        if (file.exists()) {
            //read in from the file
            val existingJson = file.readText()
            //check to see if file is not empty
            if (existingJson.isNotEmpty()) {
                //create a jsonArray of the json objects in the file
                val jsonArray = JsonParser.parseString(existingJson).asJsonArray
                //loop trough each item in json array to create a task, and add it to existing task list
                for (i in jsonArray) {
                    val task = gson.fromJson(i, Task::class.java)
                    //check for duplicate ids
                    if (updatedList.none { it.id == task.id }) {
                        //add tasks to existing task
                        existingTasks.add(task)
                    }
                }
                //add all existing tasks to the updated list
                updatedList.addAll(existingTasks)
            } else {
                println("No tasks to load")
            }

            //load updated list to viewmodel list
            _tasks.value = updatedList
        }
    }
}
