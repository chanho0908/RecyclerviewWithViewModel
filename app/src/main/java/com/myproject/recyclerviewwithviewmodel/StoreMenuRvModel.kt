package com.myproject.recyclerviewwithviewmodel

import android.graphics.Bitmap

data class StoreMenuRvModel (
    var imgBitmap: Bitmap? = null,
    var productName: String? = null,
    var productQuantity : Int? = null,
    var productIntro: String? = null,
)