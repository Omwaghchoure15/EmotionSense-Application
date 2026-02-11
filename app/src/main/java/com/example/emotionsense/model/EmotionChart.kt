package com.example.emotionsense.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.emotionsense.data.EmotionEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.forEach

@Composable
fun EmotionChart(entries: List<EmotionEntry>) {

    val grouped = entries.groupingBy { it.emotion }.eachCount()

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Emotion Summary",
            fontWeight = FontWeight.Bold
        )

        grouped.forEach { (emotion, count) ->
            Text("$emotion : $count")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Emotion Timeline",
            fontWeight = FontWeight.Bold
        )

        entries.forEach {
            val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                .format(Date(it.timestamp))

            Text("${it.emotion} at $time")
        }
    }
}

