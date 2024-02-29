package com.myproject.recyclerviewwithviewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StoreMenuManagementViewModel: ViewModel() {
    private var _menuList = MutableStateFlow<ArrayList<StoreMenuRvModel>?>(null)
    val menuList = _menuList.asStateFlow()

    fun requestStoreMenuInput(){

    }

}