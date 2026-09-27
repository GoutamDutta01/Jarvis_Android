package com.goutamdutta.jarvis.ai

import org.json.JSONObject

data class JarvisAction(val type: String, val value: String = "")

data class JarvisActionPlan(val actions: List<JarvisAction>) {
    companion object {
        fun fromJson(json: JSONObject): JarvisActionPlan {
            val result = mutableListOf<JarvisAction>()
            val actions = json.optJSONArray("actions") ?: return JarvisActionPlan(emptyList())
            for (i in 0 until actions.length()) {
                val item = actions.optJSONObject(i) ?: continue
                val type = item.optString("type").trim()
                if (type.isNotEmpty()) result += JarvisAction(type, item.optString("value").trim())
            }
            return JarvisActionPlan(result)
        }
    }
}
