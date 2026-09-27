package com.goutamdutta.jarvis.device

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract

class ContactController(private val context: Context) {
    fun dialContact(name: String): Boolean {
        val number = findNumber(name) ?: return false
        return runCatching {
            context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${Uri.encode(number)}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            true
        }.getOrDefault(false)
    }

    private fun findNumber(name: String): String? {
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val selection = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?"
        context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, arrayOf("%$name%"), null)?.use { cursor ->
            if (cursor.moveToFirst()) return cursor.getString(0)
        }
        return null
    }
}
