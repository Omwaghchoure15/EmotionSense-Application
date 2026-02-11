package com.example.emotionsense.data

fun detectEmotion(text: String): String {
    val lower = text.lowercase()
    
    val emotionKeywords = mapOf(
        "HAPPY" to listOf("happy", "great", "good", "excited", "joy", "wonderful", "amazing", "love", "awesome", "fantastic", "glad", "cheerful", "positive"),
        "SAD" to listOf("sad", "bad", "upset", "unhappy", "cry", "depressed", "lonely", "sorry", "gloomy", "miserable", "heartbroken"),
        "ANGRY" to listOf("angry", "hate", "annoyed", "furious", "mad", "irritated", "frustrated", "rage", "outrage"),
        "FEAR" to listOf("fear", "scared", "afraid", "worried", "anxious", "nervous", "terrified", "panic", "horror"),
        "SURPRISE" to listOf("surprise", "wow", "amazing", "unexpected", "shook", "shocked", "omg", "whoa"),
        "DISGUST" to listOf("disgust", "gross", "eww", "nasty", "revolting", "yuck", "sick")
    )

    for ((emotion, keywords) in emotionKeywords) {
        if (keywords.any { lower.contains(it) }) {
            return emotion
        }
    }

    return "NEUTRAL"
}
