package com.goutamdutta.jarvis.ai

class JarvisActionPlanner {
    private val parser = JarvisIntentParser()

    fun plan(command: String): JarvisActionPlan = when (val intent = parser.parse(command)) {
        is JarvisIntent.OpenApp -> planOne("open_app", intent.name)
        is JarvisIntent.GoogleSearch -> planOne("google_search", intent.query)
        is JarvisIntent.YouTubeSearch -> planOne("youtube_search", intent.query)
        is JarvisIntent.MapsSearch -> planOne("maps_search", intent.query)
        is JarvisIntent.Navigate -> planOne("navigate", intent.destination)
        is JarvisIntent.Call -> planOne("call", intent.target)
        JarvisIntent.TakePhoto -> planOne("take_photo")
        JarvisIntent.RecordVideo -> planOne("record_video")
        JarvisIntent.PlayPause -> planOne("play_pause")
        JarvisIntent.NextTrack -> planOne("next_track")
        JarvisIntent.PreviousTrack -> planOne("previous_track")
        JarvisIntent.OpenSettings -> planOne("open_settings")
        JarvisIntent.Home -> planOne("home")
        JarvisIntent.Back -> planOne("back")
        is JarvisIntent.SetAlarm -> planOne("set_alarm", "${intent.hour}:${intent.minute}:${intent.label}")
        is JarvisIntent.Unknown -> JarvisActionPlan(emptyList())
    }

    private fun planOne(type: String, value: String = "") = JarvisActionPlan(listOf(JarvisAction(type, value)))
}
