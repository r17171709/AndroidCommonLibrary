package com.renyu.androidcommonlibrary.contentobserver

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler

class SMSContentObserver(val context: Context, handler: Handler) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        sendSmsCode()
    }

    private fun sendSmsCode() {
        val cursor = context.contentResolver.query(
            Uri.parse("content://sms/inbox"),
            arrayOf("_id", "address", "read", "body", "date"), null, null, "date desc"
        )
        if (cursor != null) {
            cursor.moveToPosition(0)
            val body = cursor.getString(cursor.getColumnIndex("body"))
            val address = cursor.getString(cursor.getColumnIndex("address"))
            println("$body $address")
            cursor.close()
        }
    }
}