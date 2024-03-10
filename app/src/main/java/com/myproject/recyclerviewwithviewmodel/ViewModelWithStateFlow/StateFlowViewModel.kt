package com.myproject.recyclerviewwithviewmodel.ViewModelWithStateFlow

import android.util.Log
import androidx.lifecycle.ViewModel
import com.myproject.recyclerviewwithviewmodel.rv.StoreMenuRvModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateFlowViewModel: ViewModel() {
//    private var _menuList = MutableStateFlow<ArrayList<StoreMenuRvModel>>(ArrayList())
//    val menuList = _menuList.asStateFlow()

    val menuList = ArrayList<StoreMenuRvModel>()
    private val _menuCount = MutableStateFlow(0)
    val menuCount = _menuCount.asStateFlow()

    init {
        menuList.add(StoreMenuRvModel())
        _menuCount.value = menuList.size
    }

    fun addMenuItem() {
        // 새로운 항목 생성
//        val newItem = StoreMenuRvModel()
//        val newArrayList= _menuList.value + newItem
//        _menuList.value = newArrayList as ArrayList

        menuList.add(StoreMenuRvModel())
        Log.d("CurrentMenRvAdapter", "Add $menuList")
        _menuCount.value = menuList.size
    }

    fun deleteMenuItem(pos: Int) {
        // 데이터 리스트에 항목 제거
//        val deletedList = ArrayList<StoreMenuRvModel>()
//        deletedList.addAll(_menuList.value)
//        deletedList.removeAt(pos)
//
//        _menuList.value = deletedList
        Log.d("CurrentMenRvAdapter", "delete $menuList")

        menuList.removeAt(pos)
        _menuCount.value = menuList.size
    }
}