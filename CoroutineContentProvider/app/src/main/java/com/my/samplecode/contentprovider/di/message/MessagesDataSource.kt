package com.my.samplecode.contentprovider.di.message

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.Telephony
import com.my.samplecode.contentprovider.model.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MessagesDataSource @Inject constructor(@ApplicationContext context: Context) {
    private val contentResolver: ContentResolver = context.contentResolver

    // for tablet
    @SuppressLint("Range")
    fun fetchMessages(): List<Message> {
        val messageList = mutableListOf<Message>()
        val cursor = contentResolver.query(
            Uri.parse("content://sms/inbox"), //"content://sms/inbox"
            arrayOf(
                "_id",
                "thread_id",
                "date",
                "type",
                "address",
                "body",
                "read"
            ),
            "read = 0",
            null,
            "date DESC LIMIT 15"
        )

        // 쿼리 프로젝션이 지정한 열을 포함하는 Cursor 를 반환
        // (Cursor 는 행의 목록)
        cursor?.let { message ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                messageList.add(Message(
                    id = message.getString(message.getColumnIndex("_id")),
                    date = message.getString(message.getColumnIndex("date")),
                    address = message.getString(message.getColumnIndex("address")),
                    type = message.getString(message.getColumnIndex("type")),
                    body = message.getString(message.getColumnIndex("body")).trim(),
                    read = message.getString(message.getColumnIndex("read")),
                    name = "주소록검색해야함")
                ) //add the item
                cursor.moveToNext()
            }
            cursor.close()
        }
        return messageList.toList()
    }
}