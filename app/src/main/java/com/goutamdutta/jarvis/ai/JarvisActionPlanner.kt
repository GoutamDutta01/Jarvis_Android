package com.goutamdutta.jarvis.ai

class JarvisActionPlanner {
    private val parser = JarvisIntentParser()

    fun plan(command: String): JarvisActionPlan {
        return when (val intent = parser.parse(command)) {
            is JarvisIntent.OpenApp -> JarvisActionPlan(listOf(JarvisAction("open_app", intent.name)))
            is JarvisIntent.GoogleSearch -> JarvisActionPlan(listOf(JarvisAction("google_search", intent.query)))
            is JarvisIntent.YouTubeSearch -> JarvisActionPlan(listOf(JarvisAction("youtube_search", intent.query)))
            is JarvisIntent.MapsSearch -> JarvisActionPlan(listOf(JarvisAction("maps_search", intent.query)))
            is JarvisIntent.Navigate -> JarvisActionPlan(listOf(JarvisAction("navigate", intent.destination)))
            is JarvisIntent.Call -> JarvisActionPlan(listOf(JarvisAction("call", intent.target)))
            JarvisIntent.OpenSettings -> JarvisActionPlan(listOf(JarvisAction("open_settings")))
            JarvisIntent.Home -> JarvisActionPlan(listOf(JarvisAction("home")))
            JarvisIntent.Back -> JarvisActionPlan(listOf(JarvisAction("back")))
            is JarvisIntent.Unknown -> JarvisActionPlan(emptyList())
        }
    }
}
