package com.airs.edith.ptt.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.compositionContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.airs.edith.ptt.Greeting
import com.airs.edith.ptt.R
import com.airs.edith.ptt.common.constants.EdithAction
import com.airs.edith.ptt.common.extension.createNotification
import com.airs.edith.ptt.common.extension.createNotificationChannel
import com.airs.edith.ptt.receiver.EdithBroadcastReceiver
import com.airs.edith.ptt.ui.manager.EdithTestManager
import com.airs.edith.ptt.ui.screen.PttScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

//https://developer.android.com/guide/components/bound-services
@AndroidEntryPoint
class EdithService : LifecycleService() {
    private val TAG = this::class.simpleName
    // Binder given to clients
    private val binder = LocalBinder()
    //val edithAgent = EicAgent()
    // Random number generator
    private val mGenerator = Random()
    private val edithBroadcastReceiver = EdithBroadcastReceiver()
    @Inject
    lateinit var testManager: EdithTestManager

    /** method for clients  */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getSEdithService(): EdithService = this@EdithService
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate()")
        registerBroadcastReceiver()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService()
        }
        displayWindow()
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "onBind()")
        super.onBind(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        unregisterBroadcastReceiver()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForegroundService() {
        NotificationChannel(ServiceChannel.EDITH_ID, ServiceChannel.EDITH_NAME, NotificationManager.IMPORTANCE_DEFAULT).also { channel ->
            createNotificationChannel(channel)
        }
        createNotification(
            R.mipmap.ic_launcher_round,
            ServiceChannel.EDITH_ID,
            ServiceChannel.NOTIFICATION_EDITH_TITLE,
            ServiceChannel.NOTIFICATION_EDITH_CONTENT,
            ServiceChannel. NOTIFICATION_EDITH_TICKER
        ) {
            Intent(this, EdithService::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        }.also { notification ->
            startForeground(1, notification)
        }
    }

    private fun registerBroadcastReceiver() {
        val intent = IntentFilter().apply {
            //PBV
            addAction(EdithAction.ACTION_PBV_VR_RESTART)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)

            //T-Map
            addAction(EdithAction.ACTION_REQUEST_VR)
        }
        registerReceiver(edithBroadcastReceiver, intent)
    }

    private fun unregisterBroadcastReceiver() {
        unregisterReceiver(edithBroadcastReceiver)
    }

    private fun displayWindow() {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        // 윈도우매니저 설정
        val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,  // Android O 이상인 경우 TYPE_APPLICATION_OVERLAY 로 설정
            0 //or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.RIGHT or Gravity.TOP

        val composeView = ComposeView(this)
        composeView.setContent {
            PttScreen(testManager)
        }

        // Trick The ComposeView into thinking we are tracking lifecycle
        val viewModelStore = ViewModelStore()
        val lifecycleOwner = MyLifecycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        ViewTreeLifecycleOwner.set(composeView, lifecycleOwner)
        ViewTreeViewModelStoreOwner.set(composeView) { viewModelStore }
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)

        windowManager.addView(composeView, params)

        //recomposition
        val coroutineContext = AndroidUiDispatcher.CurrentThread
        val runRecomposeScope = CoroutineScope(coroutineContext)
        val reComposer = Recomposer(coroutineContext)
        composeView.compositionContext = reComposer
        composeView.viewTreeObserver
        runRecomposeScope.launch {
            reComposer.runRecomposeAndApplyChanges()
        }
    }
}