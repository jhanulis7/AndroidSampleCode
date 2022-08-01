package com.my.samplecode.contentprovider

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.*
import com.my.samplecode.contentprovider.ui.screen.ContactsScreen
import com.my.samplecode.contentprovider.ui.theme.CoroutineContentProviderTheme
import com.my.samplecode.contentprovider.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

//reference :
// 1. https://jossypaul.medium.com/loading-data-from-contentprovider-using-coroutines-and-livedata-34aa5e79b8ba
// 2. https://velog.io/@cchloe2311/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-ContentProvider
// MVVM : MainActivity > MainViewModel > ContactsRepository > ContactsDataSource
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CoroutineContentProviderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    requestPermission(mainViewModel)
                    //ContactsScreen(modifier = Modifier, viewModel = mainViewModel)
                    //mainViewModel.fetchContacts(Dispatchers.IO)
                }
            }
        }
    }
    
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoroutineContentProviderTheme {
        Greeting("Android")
    }
}

@Composable
fun requestPermission(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) !=
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_MMS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            RuntimePermissionScreen(mainViewModel)
        }
        else {
            ShowContactsScreen(mainViewModel)
        }
    } else {
        ShowContactsScreen(mainViewModel)
    }
}

//Reference : https://towardsdev.com/jetpack-compose-runtime-permissions-erselan-khan-75f60800b28f
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RuntimePermissionScreen(mainViewModel: MainViewModel) {
    println("RuntimePermissionScreen()")

    val context = LocalContext.current
    val singlePermissionState = rememberPermissionState(
        Manifest.permission.READ_CONTACTS
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "READ_CONTACTS Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "READ_CONTACTS Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_MMS,
        )
    ) { permissionStateMap ->
        if (!permissionStateMap.containsValue(false)) {
            Toast.makeText(context, "Location Permissions Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Location Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }

    //퍼미션 성공하고 나서 비지니스로직 화면 처리
    if (singlePermissionState.status.isGranted) {
        Toast.makeText(context, "READ_CONTACTS Permission isGranted true!!!!!!", Toast.LENGTH_SHORT).show()

        var permissionCount = 0
        multiplePermissionsState.permissions.forEach { item ->
            if (item.status.isGranted) permissionCount++
        }
        if (permissionCount == multiplePermissionsState.permissions.size) {
            ShowContactsScreen(mainViewModel)
            return
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            singlePermissionState.launchPermissionRequest()
        }) {
            Text(text = "Need Contracts Permission")
        }
        Button(onClick = {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }) {
            Text(text = "Need Location Permission")
        }
    }
}

@Composable
fun ShowContactsScreen(mainViewModel: MainViewModel) {
    ContactsScreen(modifier = Modifier, viewModel = mainViewModel)
    mainViewModel.fetchContacts(Dispatchers.IO)
    //mainViewModel.fetchBtContacts(Dispatchers.IO)
    mainViewModel.fetchMessages(Dispatchers.IO)
}