package com.goutamdutta.jarvis.ai

import android.content.Context
import com.goutamdutta.jarvis.actions.ActionRouter
import com.goutamdutta.jarvis.device.CameraController
import com.goutamdutta.jarvis.device.ContactController
import com.goutamdutta.jarvis.media.MediaController

class JarvisPlanExecutor(context: Context) {
    private val router = ActionRouter(context)
    private val camera = CameraController(context)
    private val contacts = ContactController(context)
    private val media = MediaController(context)

    fun execute(plan: JarvisActionPlan): String {
        val results = plan.actions.mapNotNull { action ->
            when (action.type) {
                "open_app" -> if (router.openApp(action.value)) "Opened ${action.value}." else "Could not open ${action.value}."
                "google_search" -> if (router.googleSearch(action.value)) "Searching Google." else "Google search failed."
                "youtube_search" -> if (router.youtubeSearch(action.value)) "Searching YouTube." else "YouTube search failed."
                "maps_search" -> if (router.mapsSearch(action.value)) "Opening Maps." else "Maps search failed."
                "navigate" -> if (router.navigate(action.value)) "Starting navigation." else "Navigation failed."
                "call" -> if (contacts.dialContact(action.value)) "Opening the dialer for ${action.value}." else "I couldn't find ${action.value} in your contacts."
                "take_photo" -> if (camera.takePhoto()) "Opening the camera." else "I couldn't open the camera."
                "record_video" -> if (camera.recordVideo()) "Opening video recording." else "I couldn't open video recording."
                "play_pause" -> if (media.playPause()) "Toggling playback." else "Media control failed."
                "next_track" -> if (media.next()) "Skipping to the next track." else "Next-track control failed."
                "previous_track" -> if (media.previous()) "Going to the previous track." else "Previous-track control failed."
                "open_settings" -> if (router.openSettings()) "Opening settings." else "Settings failed."
                "home" -> "HOME_ACTION"
                "back" -> "BACK_ACTION"
                else -> null
            }
        }
        return results.joinToString(" ").ifBlank { "I couldn't turn that request into a supported Android action." }
    }
}
