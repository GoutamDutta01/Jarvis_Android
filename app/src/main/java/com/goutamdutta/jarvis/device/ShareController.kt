package com.goutamdutta.jarvis.device

import android.content.Context
import android.content.Intent

class ShareController(private val context: Context) {
    fun shareText(text: String): Boolean = runCatching {
        context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }, "Share with..."))
        true
    }.getOrDefault(false)
}
