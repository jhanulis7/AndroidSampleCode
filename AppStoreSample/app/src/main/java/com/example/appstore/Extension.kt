package com.example.appstore

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat

fun ComponentActivity.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)
fun ComponentActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
fun ComponentActivity.requestPermissionsCompat(
    permissionsArray: Array<String>,
    requestCode: Int
) {
    ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
}


fun isQCompatibility(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
fun isRCompatibility(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
fun isSCompatibility(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
