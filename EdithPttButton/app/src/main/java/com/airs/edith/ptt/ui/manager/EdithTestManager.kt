package com.airs.edith.ptt.ui.manager

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.airs.edith.niropluspttbutton.manager.ManagerInterface
import com.airs.edith.ptt.common.constants.EdithTestMenu
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("StaticFieldLeak")
@Singleton
class EdithTestManager @Inject constructor(
    @ApplicationContext val context: Context
) : ManagerInterface {
    private var bargeIn = true

    override fun initialize() {
    }

    override fun clear() {
    }

    fun startPttButton() {
        Log.d("test", "startPttButton ")
        try {
            Intent().apply {
                action = "com.mtx.intent.action.test"
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runVrApplication() {
        Log.d("test", "runVrApplication()")

        try {
            val intent = context.packageManager.getLaunchIntentForPackage("com.airs.edith.pbv")
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.d("test", "runVrApplication $e")
        } catch (e: NullPointerException) {
            Log.d("test", "runVrApplication $e")
        }
    }

    private fun requestVrTermsAgreement() {
        Log.e("Test","requestVrTermsAgreement()")
        try {
            Intent().apply {
                action = "com.mtx.intent.action.AGREEMENT_VR_BYPASS"
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notifyCurrentSoc() {
        Log.d("test", "notifyCurrentSoc ")
        //isEdithAgentForeground()
        Toast.makeText(context, "배터리 잔량 : 14", Toast.LENGTH_SHORT).show()

        try {
            Intent().apply {
                action = "com.mtx.intent.action.EV_BMS_DISPLAY_SOC"
                putExtra("Value", 14)
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notifyRemainDistance() {
        Log.d("test", "notifyRemainDistance ")
        Toast.makeText(context, "남은 주행거리 : 40", Toast.LENGTH_SHORT).show()

        //isEdithAgentForeground()
        try {
            Intent().apply {
                action = "com.mtx.intent.action.EV_VCU_DIST_EMPTY_KM"
                putExtra("Value", 40)
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * notifySocAlert
     * 배터리 부족(14.2%)
     */
    private fun notifySocAlert() {
        Log.d("test", "notifySocAlert ")
        //isEdithAgentForeground()
        Toast.makeText(context, "SOC Alert Level1", Toast.LENGTH_SHORT).show()
        try {
            Intent().apply {
                action = "com.mtx.intent.action.SOC_ALERT"
                putExtra("Value", 1)
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notifySocAlertAfterGuide() {
        Log.d("test", "notifySocAlertAfterGuide ")
        Toast.makeText(context, "배터리 잔량 : 29", Toast.LENGTH_SHORT).show()
        //isEdithAgentForeground()
        try {
            Intent().apply {
                action = "com.mtx.intent.action.EV_BMS_DISPLAY_SOC"
                putExtra("Value", 29)
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notifyMaybeSocAlert() {
        Log.d("test", "notifyMaybeSocAlert ")
        Toast.makeText(context, "배터리 잔량 : 31", Toast.LENGTH_SHORT).show()
        //isEdithAgentForeground()
        try {
            Intent().apply {
                action = "com.mtx.intent.action.EV_BMS_DISPLAY_SOC"
                putExtra("Value", 31)
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestVrBargeIn() {
        Log.e("Test","requestVrBargeIn()")
        bargeIn = !bargeIn
        Toast.makeText(context, "VR Barge-In 설정 : $bargeIn", Toast.LENGTH_SHORT).show()

        try {
            Intent().apply {
                action = "com.mtx.intent.action.VR_BARGE_IN"
                putExtra("Value", bargeIn)
            }.also {
                context.sendBroadcast(it)
                Toast.makeText(context, "ASR 전송완료", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendAsrText(inputText: String) {
        try {
            Intent().apply {
                action = "com.mobis.splitscreen.right.ASR_TEST_COMMAND"
                putExtra("ASR", inputText)
            }.also {
                context.sendBroadcast(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun runTestMenuList(index: Int) {
        Log.d("Test",  "runTestMenuList index: $index !")
        when(index) {
            EdithTestMenu.SystemVrTermsAgreement.ordinal -> requestVrTermsAgreement()
            EdithTestMenu.ChangeBargeIn.ordinal -> requestVrBargeIn()
            EdithTestMenu.CurrentSoc.ordinal -> notifyCurrentSoc()
            EdithTestMenu.CurrentRemainDistance.ordinal -> notifyRemainDistance()
            EdithTestMenu.SocAlert1.ordinal -> notifySocAlert()
            EdithTestMenu.SocAlert2AfterGuide.ordinal -> notifySocAlertAfterGuide()
            EdithTestMenu.SocAlertMaybe.ordinal -> notifyMaybeSocAlert()
            else -> Log.e("Test",  "runTestMenuList index error!")
        }
    }
}