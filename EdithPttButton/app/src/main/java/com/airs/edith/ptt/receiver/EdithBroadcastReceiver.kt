package com.airs.edith.ptt.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class EdithBroadcastReceiver : BroadcastReceiver() {
    private val TAG = this::class.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive() : action:${intent?.action}, $intent")
        intent?.let { processIntent(context, it) }
    }

    private fun processIntent(context: Context?, intent: Intent) {
        intent.bundleLog()

        when (intent.action) {
            else -> Log.d(TAG, "There is nothing to process for '[${intent.action}]'")
        }
    }

    private fun Intent.bundleLog(){
        extras?.let { bundle ->
            bundle.keySet().forEach { key ->
                Log.d(TAG, "bundle KEY[$key] - ${bundle[key]}")
            }
        }
    }
}