package com.goutamdutta.jarvis.ai

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class JarvisBackendClient(
    private val baseUrl: String,
    private val onResult: (JarvisActionPlan?) -> Unit
) {
    private val executor = Executors.newSingleThreadExecutor()

    fun plan(command: String) {
        executor.execute {
            val plan = runCatching {
                val connection = (URL("${baseUrl.trimEnd('/')}/v1/plan").openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 10_000
                    readTimeout = 30_000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Accept", "application/json")
                }
                val request = JSONObject().put("command", command)
                connection.outputStream.use { it.write(request.toString().toByteArray()) }
                if (connection.responseCode !in 200..299) return@runCatching null
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                parsePlan(JSONObject(response))
            }.getOrNull()
            onResult(plan)
        }
    }

    private fun parsePlan(json: JSONObject): JarvisActionPlan {
        val actions = mutableListOf<JarvisAction>()
        val array = json.optJSONArray("actions") ?: JSONArray()
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val type = item.optString("type").trim()
            if (type.isNotBlank()) actions += JarvisAction(type, item.optString("value").trim())
        }
        return JarvisActionPlan(actions)
    }

    fun shutdown() = executor.shutdownNow()
}
