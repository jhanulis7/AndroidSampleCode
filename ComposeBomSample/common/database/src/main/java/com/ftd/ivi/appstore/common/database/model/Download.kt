package com.ftd.ivi.appstore.common.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
class Download {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "downloadId")
    var id: Int = 0

    @ColumnInfo(name = "packageName")
    var packageName: String = ""
    var size: Int = 0

    constructor() {}

    constructor(id: Int, packageName: String, size: Int) {
        this.id = id
        this.packageName = packageName
        this.size = size
    }

    constructor(settingName: String, size: Int) {
        this.packageName = settingName
        this.size = size
    }
}
