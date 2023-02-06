package com.example.appstore

import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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