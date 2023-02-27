package com.ftd.ivi.appstore.common.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ftd.ivi.appstore.common.database.model.Download
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads")
    fun getAllDownloads(): Flow<List<Download>>

    @Query("SELECT * FROM downloads WHERE packageName = :packageName")
    fun searchDownloads(packageName: String): Flow<List<Download>>

    @Insert
    suspend fun insertDownload(download: Download)

    @Update
    suspend fun updateDownload(download: Download)

    @Query("DELETE FROM downloads WHERE packageName = :packageName")
    suspend fun deleteDownload(packageName: String)

    @Query("DELETE FROM downloads")
    suspend fun deleteAll()
}