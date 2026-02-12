# EmotionSense-Application

EmotionSense is an Android application that captures voice input, analyzes emotional sentiment, and visualizes emotion trends over time.

Built using **Kotlin** and **Jetpack Compose**.

---

## Features

- 🎤 Voice input using Android SpeechRecognizer  
- 🧠 Real-time emotion detection (rule-based sentiment analysis)  
- 📊 Emotion summary visualization  
- 🕒 Emotion timeline with accurate recording timestamps  
- 📦 Clean and simple Jetpack Compose UI  
- 🔐 Runtime microphone permission handling  

---

## Tech Stack

- Kotlin
- Jetpack Compose
- Android SpeechRecognizer API
- Material 3
- State management using `mutableStateOf`

---

## How It Works

1. User records voice input.
2. SpeechRecognizer converts speech to text.
3. The text is analyzed using a rule-based emotion classifier.
4. The detected emotion is stored along with the exact recording timestamp.
5. The app displays:
   - Emotion summary (count of each emotion)
   - Emotion timeline (emotion + time of occurrence)

---

## Emotion Detection Logic

The current implementation uses a simple rule-based classification:

- "happy", "great", "good", "excited" → **HAPPY**
- "sad", "bad", "upset" → **SAD**
- "angry", "hate", "annoyed" → **ANGRY**
- Default → **NEUTRAL**

This architecture allows future integration with:
- TensorFlow Lite models
- HuggingFace NLP APIs
- Advanced ML-based sentiment classifiers

---

## Output Example

- Emotion Summary:
  - HAPPY: 2
  - SAD: 1
  - ANGRY: 1

- Emotion Timeline:
  - HAPPY at 20:13:52
  - SAD at 20:14:01
  - ANGRY at 20:14:10

---

## Future Improvements

- Integrate ML-based sentiment model
- Persist emotion history using Room Database
- Improve UI with animated charts
- Export emotion analytics
- Add weekly/monthly emotion trends

---

## APK

The release APK is included for installation and testing.

---

## 👨‍💻 Developer

**Om Waghchoure**  
Android Developer | Kotlin | Jetpack Compose

That README makes you look structured and thoughtful.
