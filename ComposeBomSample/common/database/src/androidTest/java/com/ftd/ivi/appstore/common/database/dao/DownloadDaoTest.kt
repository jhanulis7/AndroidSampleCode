package com.ftd.ivi.appstore.common.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ftd.ivi.appstore.common.database.AppDatabase
import com.ftd.ivi.appstore.common.database.model.Download
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DownloadDaoTest {
    private lateinit var downloadDao: DownloadDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
        downloadDao = db.downloadDao()
    }

    @Test
    fun downloadDao_insert() = runTest {
        val downloadEntities = listOf(
            Download(
                "com.unitest.one",
                100,
            ),
            Download(
                "com.unitest.two",
                200,
            ),
        )
        downloadEntities.forEach { download ->
            downloadDao.insertDownload(download)
        }

        val searchDownload = downloadDao.searchDownloads("com.unitest.one").first()

        Assert.assertNotEquals(
            searchDownload.size,
            0
        )
        Assert.assertEquals(
            searchDownload[0].packageName,
            "com.unitest.one"
        )
    }
}