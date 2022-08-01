package com.my.samplecode.contentprovider.di.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.my.samplecode.contentprovider.model.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactsBtDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val contentResolver: ContentResolver = context.contentResolver

    /**
     * Fetch contacts
     * 전화번호가 있는 리스트만 들고 옴
     * @return
     */
    @SuppressLint("Range")
    fun fetchContacts(): List<Contact> {
        val result: MutableList<Contact> = mutableListOf()

        /**
         * ContentResolver
         * - 이 객체를 사용하여 클라이언트로서 제공자와 통신을 주고받음
         */
        val cursor = contentResolver.query(
            Uri.parse("content://com.android.contacts_bt/contacts"),
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
                ContactsContract.CommonDataKinds.Phone._ID
            ), // 가져올 컬럼의 이름 목록
            null, // where 절
            null, // selection 에서 ? 로 표시 곳에 들어갈 데이터
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME // order by
        )

        // 쿼리 프로젝션이 지정한 열을 포함하는 Cursor 를 반환
        // (Cursor 는 행의 목록)
        cursor?.let {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                result.add(
                    Contact(
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)),
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)),
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: "",
                    )
                ) //add the item
                cursor.moveToNext()
            }
            cursor.close()
        }

        return result.toList()
    }
}