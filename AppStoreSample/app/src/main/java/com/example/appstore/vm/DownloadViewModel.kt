package com.example.appstore.vm

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appstore.BuildConfig
import com.example.appstore.R
import com.example.appstore.isNCompatibility
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class DownloadViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    fun enqueueDownload(url: String, onComplete: () -> Unit) {
        var destination =
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += FILE_NAME
        val uri = Uri.parse("$FILE_BASE_PATH$destination")
        val file = File(destination)
        if (file.exists()) file.delete()
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setMimeType(MIME_TYPE)
        request.setTitle(context.getString(R.string.title_file_download))
        request.setDescription(context.getString(R.string.downloading))
        // set destination
        request.setDestinationUri(uri)
        showInstallOption(destination, uri, onComplete)
        // Enqueue a new download and same the referenceId
        downloadManager.enqueue(request)
    }

    private fun showInstallOption(
        destination: String,
        uri: Uri,
        onDownloadComplete: () -> Unit
    ) {
        // set BroadcastReceiver to install app when .apk is downloaded
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                if (isNCompatibility()) {
                    val contentUri = FileProvider.getUriForFile(
                        context,
                        BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                        File(destination)
                    )
                    val install = Intent(Intent.ACTION_VIEW)
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                    install.data = contentUri
                    context.startActivity(install)
                    context.unregisterReceiver(this)
                    // finish()
                } else {
                    val install = Intent(Intent.ACTION_VIEW)
                    install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    install.setDataAndType(
                        uri,
                        APP_INSTALL_PATH
                    )
                    context.startActivity(install)
                    context.unregisterReceiver(this)
                    // finish()
                }
                onDownloadComplete()
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun installAssetApk() {
        val path = context.filesDir.absolutePath + "/app.apk"
        val file = File(path)
        if (!file.exists()) {
            Log.e("AppStoreSample", " There is no file:$path")
            Toast.makeText(context, "There is no file!!", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )

        Log.d("AppStoreSample", "installAssetApk() path:$path, uri:$uri")
        Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "application/vnd.android.package-archive")
        }.run {
            context.startActivity(this)
        }
    }

    private fun registerInstaller(sessionId: Int) {
        val installer = context.packageManager.packageInstaller
        installer.registerSessionCallback(object : PackageInstaller.SessionCallback() {
            override fun onCreated(sessionid: Int) {
                Log.d("AppStore", "onCreated: installer created")
            }

            override fun onBadgingChanged(p0: Int) {
                Log.d("AppStore", "onBadgingChanged")
            }

            override fun onActiveChanged(p0: Int, p1: Boolean) {
                Log.d("AppStore", "onActiveChanged")
            }

            override fun onProgressChanged(p0: Int, p1: Float) {
                Log.d("AppStore", "onProgressChanged p0:$p0, p1:$p1")
            }

            override fun onFinished(sessionid: Int, success: Boolean) {
                if (sessionid != sessionId) return
                if (success) {
                    Log.d("AppStore", "onFinished: installation successful! sessionid:$sessionid")
                    Toast.makeText(context,
                        context.resources.getString(R.string.install_complete), Toast.LENGTH_LONG).show()
                } else {
                    Log.d("AppStore", "onFinished: installation failed")
                }
            }
        })
    }
    fun installApkNoUnknownSource(inputStream: InputStream, onComplete: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val installer = context.packageManager.packageInstaller
            val params =
                PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId = installer.createSession(params)
            val session = installer.openSession(sessionId)

            val out = session.openWrite("COSU", 0, -1)
            val buffer = ByteArray(65536)
            do {
                val c = inputStream.read(buffer)
                Log.d("AppStore", "read: $c")
                if (c == -1) break
                out.write(buffer, 0, c)
            } while (true)

            session.fsync(out)
            //inputStream.close()
            onComplete.invoke()
            out.close()

            withContext(Dispatchers.Main) {
                registerInstaller(sessionId)
            }

            PendingIntent.getBroadcast(
                context,
                sessionId,
                Intent(Intent.ACTION_PACKAGE_ADDED),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            ).run {
                session.commit(intentSender)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun uninstallServerDownloadApp() {
        val packageURI = Uri.parse("package:com.starbucks.co")
        try {
            Intent(Intent.ACTION_DELETE, packageURI).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }.run {
                context.startActivity(this)
            }
        } catch (e: Exception) {
            Log.e("AppStore", "${e.message}")
        }
    }

    fun uninstallAssetADownloadApp() {
        val packageURI = Uri.parse("package:com.komorebi.memo")
        try {
            Intent(Intent.ACTION_DELETE, packageURI).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }.run {
                context.startActivity(this)
            }
        } catch (e: Exception) {
            Log.e("AppStore", "${e.message}")
        }
    }

    companion object {
        private const val FILE_NAME = "SampleDownloadApp2.apk"
        private const val FILE_BASE_PATH = "file://"
        private const val MIME_TYPE = "application/vnd.android.package-archive"
        private const val PROVIDER_PATH = ".provider"
        private const val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
    }
}