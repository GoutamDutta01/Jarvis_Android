package com.goutamdutta.jarvis.media

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.media.AudioManager

class MediaController(private val context: Context) {
    private val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun playPause(): Boolean = send(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
    fun next(): Boolean = send(KeyEvent.KEYCODE_MEDIA_NEXT)
    fun previous(): Boolean = send(KeyEvent.KEYCODE_MEDIA_PREVIOUS)

    private fun send(keyCode: Int): Boolean = runCatching {
        audio.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
        audio.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
        true
    }.getOrDefault(false)
}
