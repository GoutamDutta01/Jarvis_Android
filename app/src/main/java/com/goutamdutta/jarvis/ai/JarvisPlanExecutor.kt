package com.goutamdutta.jarvis.ai

import android.content.Context
import com.goutamdutta.jarvis.actions.ActionRouter

class JarvisPlanExecutor(context: Context) {
    private val router = ActionRouter(context)

    fun execute(plan: JarvisActionPlan): String {
        val results = plan.actions.mapNotNull { action ->
            when (action.type) {
                "open_app" -> if (router.openApp(action.value)) "Opened ${action.value}." else "Could not open ${action.value}."
                "google_search" -> if (router.googleSearch(action.value)) "Searching Google." else "Google search failed."
                "youtube_search" -> if (router.youtubeSearch(action.value)) "Searching YouTube." else "YouTube search failed."
                "maps_search" -> if (router.mapsSearch(action.value)) "Opening Maps." else "Maps search failed."
                "navigate" -> if (router.navigate(action.value)) "Starting navigation." else "Navigation failed."
                "call" -> if (router.callContact(action.value)) "Opening the dialer." else "I couldn't resolve that number."
                "open_settings" -> if (router.openSettings()) "Opening settings." else "Settings failed."
                "home" -> "HOME_ACTION"
                "back" -> "BACK_ACTION"
                else -> null
            }
        }
        return results.joinToString(" ").ifBlank { "I couldn't turn that request into a supported Android action." }
    }
}
