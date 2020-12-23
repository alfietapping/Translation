package com.tapmaxdev.translation.util

import com.tapmaxdev.translation.R
import com.tapmaxdev.translation.model.LanguageModel

fun createLanguageModelList(): List<LanguageModel> {
    val list = mutableListOf<LanguageModel>()
    list.add(LanguageModel(R.drawable.south_africa, "Afrikaans"))
    list.add(LanguageModel(R.drawable.bulgaria, "Bulgarian"))
    list.add(LanguageModel(R.drawable.china, "Chinese"))
    list.add(LanguageModel(R.drawable.croatia, "Croatian"))
    list.add(LanguageModel(R.drawable.czech_republic, "Czech"))
    list.add(LanguageModel(R.drawable.denmark, "Danish"))
    list.add(LanguageModel(R.drawable.netherlands, "Dutch"))
    list.add(LanguageModel(R.drawable.england, "English"))
    list.add(LanguageModel(R.drawable.finland, "Finnish"))
    list.add(LanguageModel(R.drawable.france, "French"))
    list.add(LanguageModel(R.drawable.germany, "German"))
    list.add(LanguageModel(R.drawable.greece, "Greek"))
    list.add(LanguageModel(R.drawable.india, "Hindi"))
    list.add(LanguageModel(R.drawable.hungary, "Hungarian"))
    list.add(LanguageModel(R.drawable.italy, "Italian"))
    list.add(LanguageModel(R.drawable.japan, "Japanese"))
    list.add(LanguageModel(R.drawable.south_korea, "Korean"))
    list.add(LanguageModel(R.drawable.norway, "Norwegian"))
    list.add(LanguageModel(R.drawable.portugal, "Portuguese"))
    list.add(LanguageModel(R.drawable.poland, "Polish"))
    list.add(LanguageModel(R.drawable.romania, "Romanian"))
    list.add(LanguageModel(R.drawable.slovakia, "Slovakian"))
    list.add(LanguageModel(R.drawable.sweden, "Swedish"))
    list.add(LanguageModel(R.drawable.thailand, "Thai"))
    list.add(LanguageModel(R.drawable.turkey, "Turkish"))
    list.add(LanguageModel(R.drawable.ukraine, "Ukrainian"))
    list.add(LanguageModel(R.drawable.spain, "Spanish"))

    return list
}

fun decideLanguage(language: String?): String {
    return when (language) {
        "Afrikaans" -> "af"
        "Bulgarian" -> "bg"
        "Chinese" -> "zh"
        "Croatian" -> "hr"
        "Czech" -> "cs"
        "Danish" -> "da"
        "Dutch" -> "da"
        "English" -> "en"
        "Finnish" -> "fi"
        "French" -> "fr"
        "German" -> "de"
        "Greek" -> "el"
        "Hindi" -> "hi"
        "Hungarian" -> "hu"
        "Italian" -> "it"
        "Japanese" -> "ja"
        "Korean" -> "ko"
        "Norwegian" -> "no"
        "Portuguese" -> "pt"
        "Polish" -> "pl"
        "Romanian" -> "ro"
        "Slovakian" -> "sl"
        "Swedish" -> "sv"
        "Thai" -> "th"
        "Turkish" -> "tr"
        "Ukrainian" -> "uk"
        else -> "es"
    }
}
