package com.my.samplecode.contentprovider.di.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import com.my.samplecode.contentprovider.model.Contact

class ContactsDataSource constructor(
    private val contentResolver: ContentResolver
) {
    // 별도의 스레드에서 비동기식으로 쿼리를 실행해야 함
    // -> 보통 CursorLoader 클래스를 사용, 여기서는 코루틴 사용
    @SuppressLint("Range")
    fun fetchContacts(): List<Contact> {
        val result: MutableList<Contact> = mutableListOf()

        /**
         * ContentResolver
         * - 이 객체를 사용하여 클라이언트로서 제공자와 통신을 주고받음
         * - ContentProvider 를 구현한 클래스의 인스턴스와 통신
         * ContactsContract.Contacts.CONTENT_URI 와
         * ContactsContract.Data.CONTENT_URI 차이가 뭘까
         *
         */
        val cursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI, // 원하는 데이터를 가져올 주소
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
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
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)),
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

fun String.toContactImageUri() = Uri.withAppendedPath(
    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, this.toLong()),
    ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
).toString()