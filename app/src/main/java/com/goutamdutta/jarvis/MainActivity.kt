package com.goutamdutta.jarvis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.goutamdutta.jarvis.ai.JarvisBackendClient
import com.goutamdutta.jarvis.ai.JarvisPlanExecutor
import com.goutamdutta.jarvis.voice.JarvisSpeechController

class MainActivity : ComponentActivity() {
    private var status by mutableStateOf("Ready")
    private lateinit var speech: JarvisSpeechController
    private lateinit var localExecutor: JarvisPlanExecutor
    private lateinit var backend: JarvisBackendClient

    private val microphonePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) listen() else status = "Microphone permission is required."
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localExecutor = JarvisPlanExecutor(this)
        backend = JarvisBackendClient(BuildConfig.JARVIS_BACKEND_URL) { plan ->
            runOnUiThread {
                status = if (plan == null) "AI planning failed; try again." else localExecutor.execute(plan)
            }
        }
        speech = JarvisSpeechController(this, onCommand = { command ->
            status = "Planning: $command"
            backend.plan(command)
        }, onError = { status = it })

        setContent {
            MaterialTheme {
                Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Jarvis Android", style = MaterialTheme.typography.headlineMedium)
                    Text(status)
                    Button(onClick = {
                        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) listen()
                        else microphonePermission.launch(Manifest.permission.RECORD_AUDIO)
                    }) { Text("Speak Command") }
                    Button(onClick = { startActivity(Intent(Settings.ACTION_VOICE_INPUT_SETTINGS)) }) { Text("Voice Assistant Settings") }
                    Button(onClick = { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }) { Text("Accessibility Settings") }
                }
            }
        }
    }

    private fun listen() {
        status = "Listening..."
        speech.start()
    }

    override fun onDestroy() {
        speech.destroy()
        backend.shutdown()
        super.onDestroy()
    }
}
