package com.school.tasktracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


// ViewModel is going to be used for sharing data across views in a unified way
// And for storing important data in general like Task()

// There will be a lot of example data for now because I am focusing on the UI currently
class MainViewModel: ViewModel() {

    private var _infoValue = MutableLiveData(List(4) { false })
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
        // ?: is the Elvis operator, means if the value preceding ?: is null then insert the change the value to the right of ?:
        // https://stackoverflow.com/questions/48253107/what-does-do-in-kotlin-elvis-operator
        _viewNumber.value = newNumber
    }

    fun updateTheme(themeName: String) {
        val themes = arrayOf("default", "dark", "light")

        if (themes.contains(themeName)) {
            _theme.value = themeName
        }
    }
}
