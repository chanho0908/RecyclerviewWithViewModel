package com.myproject.recyclerviewwithviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class StoreMenuManagementViewModel: ViewModel() {
    private var _menuList = MutableStateFlow<ArrayList<StoreMenuRvModel>>(ArrayList())
    val menuList = _menuList.asStateFlow()

    init {
        _menuList.value.add(StoreMenuRvModel())
    }

    fun addMenuItem() {
        Log.d("CurrentMenRvAdapter", "addMenuItem()")
        // 새로운 항목 생성
        val newItem = StoreMenuRvModel()
        _menuList.value.add(newItem)

        Log.d("CurrentMenRvAdapter", "Current MenuList : ${menuList.value.size}")

    }

    fun deleteMenuItem(pos: Int) {
        Log.d("CurrentMenRvAdapter", "addMenuItem()")

        // 데이터 리스트에 항목 제거
        _menuList.value?.removeAt(pos)
        menuList.map { Log.d("CurrentMenRvAdapter", "Current MenuList : $it") }
    }
}