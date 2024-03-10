package com.myproject.recyclerviewwithviewmodel.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
fun Context.hasImagePermission(): Boolean = Utils.REQUEST_IMAGE_PERMISSIONS.any { this.hasPermission(it) }

fun Context.showPermissionSnackBar(view: View) {
    Snackbar.make(view, "권한이 거부 되었습니다. 설정(앱 정보)에서 권한을 확인해 주세요.",
        Snackbar.LENGTH_SHORT
    ).setAction("확인"){
        //설정 화면으로 이동
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val packageName = this.packageName
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri

        this.startActivity(intent)

    }.show()
}


fun Context.showSoftInput(focusView: TextInputEditText) {
    val inputMethodManager = this@showSoftInput.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE)
            as InputMethodManager
    inputMethodManager.showSoftInput(focusView, 0)
}

fun Context.hideSoftInput(focusView: TextInputEditText) {
    val inputMethodManager = this@hideSoftInput.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE)
            as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)

}