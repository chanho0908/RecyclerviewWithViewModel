package com.myproject.recyclerviewwithviewmodel.rv

import android.graphics.Bitmap

data class StoreMenuRvModel (
    var imgBitmap: Bitmap? = null,
    var productName: String? = null,
    var productQuantity : Int = 0,
    var productIntro: String? = null,
)