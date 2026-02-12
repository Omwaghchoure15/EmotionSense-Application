package com.example.emotionsense.ui.screen

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.emotionsense.data.EmotionEntry
import com.example.emotionsense.data.detectEmotion
import com.example.emotionsense.model.EmotionChart
import com.example.emotionsense.ui.theme.EmotionSenseTheme

@Composable
fun HomeScreen(
    speechRecognizer: Any?, // Use Any? to avoid ClassNotFoundException: android.speech.SpeechRecognizer in Compose Preview
    speechIntent: Intent?
) {
    val recognizer = speechRecognizer as? SpeechRecognizer
    var spokenText by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }
    val emotionList = remember { mutableStateListOf<EmotionEntry>() }
    var recordingTimestamp by remember { mutableStateOf<Long?>(null) }

    HomeScreenContent(
        spokenText = spokenText,
        isListening = isListening,
        emotionList = emotionList,
        onToggleListening = {
            if (recognizer != null && speechIntent != null) {
                if (!isListening) {
                    isListening = true
                    recognizer.setRecognitionListener(object : RecognitionListener {
                        override fun onResults(results: Bundle?) {
                            val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            spokenText = data?.firstOrNull() ?: ""
                            recordingTimestamp = System.currentTimeMillis()
                            isListening = false
                        }

                        override fun onPartialResults(partialResults: Bundle?) {
                            val data = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            spokenText = data?.firstOrNull() ?: ""
                        }

                        override fun onReadyForSpeech(params: Bundle?) {}
                        override fun onBeginningOfSpeech() {
                            recordingTimestamp = System.currentTimeMillis()
                        }
                        override fun onRmsChanged(rmsdB: Float) {}
                        override fun onBufferReceived(buffer: ByteArray?) {}
                        override fun onEndOfSpeech() { isListening = false }
                        override fun onError(error: Int) {
                            Log.e("SpeechRecognizer", "Error: $error")
                            isListening = false
                        }
                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                    recognizer.startListening(speechIntent)
                } else {
                    recognizer.stopListening()
                    isListening = false
                }
            }
        },
        onAnalyze = {
            if (spokenText.isNotEmpty()) {
                val timestamp = recordingTimestamp ?: System.currentTimeMillis()
                val emotion = detectEmotion(spokenText)
                emotionList.add(0, EmotionEntry(emotion, timestamp))
                // Reset spoken text after analysis
                spokenText = ""
                recordingTimestamp = null
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    spokenText: String,
    isListening: Boolean,
    emotionList: List<EmotionEntry>,
    onToggleListening: () -> Unit,
    onAnalyze: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("EmotionSense", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Speech Input Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Spoken Words",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (spokenText.isEmpty()) "Start speaking to see text here..." else spokenText,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        minLines = 3
                    )
                }
            }

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onToggleListening,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isListening) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isListening) "Stop" else "Record")
                }

                ElevatedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onAnalyze,
                    enabled = spokenText.isNotEmpty()
                ) {
                    Icon(Icons.Default.Analytics, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyze")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Emotion Insights",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            EmotionChart(emotionList)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Created by Om Waghchoure\nAndroid Developer",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    EmotionSenseTheme {
        HomeScreenContent(
            spokenText = "I am feeling very happy today!",
            isListening = false,
            emotionList = listOf(
                EmotionEntry("Happy", System.currentTimeMillis()),
                EmotionEntry("Neutral", System.currentTimeMillis() - 60000)
            ),
            onToggleListening = {},
            onAnalyze = {}
        )
    }
}
