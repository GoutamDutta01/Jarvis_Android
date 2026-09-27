package com.goutamdutta.jarvis.device

import android.content.Context
import android.content.Intent
import android.provider.MediaStore

class CameraController(private val context: Context) {
    fun takePhoto(): Boolean = runCatching {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        true
    }.getOrDefault(false)

    fun recordVideo(): Boolean = runCatching {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        true
    }.getOrDefault(false)
}
