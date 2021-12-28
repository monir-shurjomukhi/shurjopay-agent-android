package com.sm.spagent.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sm.spagent.modelroomdb.ModelHomeMenu

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    private fun getHomeMenuData(): MutableLiveData<List<ModelHomeMenu>> {
        val mDeveloperModel: List<ModelHomeMenu> = listOf(
            ModelHomeMenu("Attendance", null, null),
            ModelHomeMenu("Change Password", null, null),
            ModelHomeMenu("Recommendation", null, null),
            ModelHomeMenu("New Merchandiser", null, null),
            ModelHomeMenu("Merchandiser List", null, null),
        )
        val mutableDataList = MutableLiveData<List<ModelHomeMenu>>()
        mutableDataList.value = mDeveloperModel
        return mutableDataList
    }
    val homeMenuData: LiveData<List<ModelHomeMenu>> = getHomeMenuData()
}