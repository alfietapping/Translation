package com.tapmaxdev.translation.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.tapmaxdev.translation.R
import com.tapmaxdev.translation.adapter.LanguageAdapter1
import com.tapmaxdev.translation.adapter.LanguageAdapter2
import com.tapmaxdev.translation.model.LanguageModel
import kotlinx.android.synthetic.main.activity_translator.*
import java.util.*
import com.tapmaxdev.translation.util.createLanguageModelList
import com.tapmaxdev.translation.util.decideLanguage
import kotlinx.android.synthetic.main.dialog_languages.view.*

class TranslatorActivity : AppCompatActivity() {

    private var language1: String? = null
    private var language2: String? = null

    companion object {
        private const val LANGUAGE1CODE = 1
        private const val LANGUAGE2CODE = 2
    }

    private var textToSpeech1: TextToSpeech? = null
    private var textToSpeech2: TextToSpeech? = null

    private fun languageOptions1(language1: String?, language2: String?) = TranslatorOptions.Builder()
        .setSourceLanguage(decideLanguage(language1))
        .setTargetLanguage(decideLanguage(language2))
        .build()

    private fun languageOptions2(language2: String?, language1: String?) = TranslatorOptions.Builder()
        .setSourceLanguage(decideLanguage(language2))
        .setTargetLanguage(decideLanguage(language1))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translator)

        language1ImageView.setOnClickListener {
            buildLanguagesRecyclerDialog1()
        }

        language2ImageView.setOnClickListener {
            buildLanguagesRecyclerDialog2()
        }

        changeLanguage1ImageView.setOnClickListener {
            buildLanguagesRecyclerDialog1()
        }

        changeLanguage2ImageView.setOnClickListener {
            buildLanguagesRecyclerDialog2()
        }

        startTranslationImageView.setOnClickListener {
            buildFirstSpeechRecognizer(language1)
        }
    }

    private fun itemClick1(languageModel: LanguageModel) {
        language1ImageView.setImageResource(languageModel.flag)
        language1TextView.text = languageModel.language
        language1 = languageModel.language
        changeLanguage1ImageView.visibility = View.VISIBLE
    }

    private fun itemClick2(languageModel: LanguageModel) {
        language2ImageView.setImageResource(languageModel.flag)
        language2TextView.text = languageModel.language
        language2 = languageModel.language
        changeLanguage2ImageView.visibility = View.VISIBLE
    }

    private fun buildLanguagesRecyclerDialog1() {
        val alertBuilder: AlertDialog = AlertDialog.Builder(this).create()
        val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(R.layout.dialog_languages, null)
        alertBuilder.setView(view)
        setLanguagesRecycler1(view, alertBuilder)
        alertBuilder.show()
    }

    private fun buildLanguagesRecyclerDialog2() {
        val alertBuilder: AlertDialog = AlertDialog.Builder(this).create()
        val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(R.layout.dialog_languages, null)
        alertBuilder.setView(view)
        setLanguagesRecycler2(view, alertBuilder)
        alertBuilder.show()
    }
    
    private fun setLanguagesRecycler1(view: View, alertDialog: AlertDialog) {
        val languageAdapter = LanguageAdapter1(::itemClick1, alertDialog)
        languageAdapter.submitList(createLanguageModelList())

        view.languagesRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = languageAdapter
        }
    }

    private fun setLanguagesRecycler2(view: View, alertDialog: AlertDialog) {
        val languageAdapter = LanguageAdapter2(::itemClick2, alertDialog)
        languageAdapter.submitList(createLanguageModelList())

        view.languagesRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = languageAdapter
        }
    }

    private fun initFirstTextToSpeech(language: String?) {
       textToSpeech1 = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech1?.language = if (decideLanguage(language) == "en") {
                    Locale.UK
                } else {
                    Locale(decideLanguage(language))
                }
            }

           textToSpeech1?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
               override fun onStart(p0: String?) {
               }

               override fun onDone(p0: String?) {
                   buildSecondSpeechRecognizer(language2)
               }

               override fun onError(p0: String?) {
               }

           })
        })
    }

    private fun initSecondTextToSpeech(language: String?) {
        textToSpeech2 = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech2?.language = if (decideLanguage(language) == "en") {
                    Locale.UK
                } else {
                    Locale(decideLanguage(language))
                }
            }

            textToSpeech2?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(p0: String?) {
                }

                override fun onDone(p0: String?) {
                    buildFirstSpeechRecognizer(language1)
                }

                override fun onError(p0: String?) {
                }
            })
        })
    }

    private fun buildFirstSpeechRecognizer(language: String?) {
        initFirstTextToSpeech(language2)
        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, decideLanguage(language))
        sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
        try {
            startActivityForResult(sttIntent, LANGUAGE1CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "Your device does not support STT.", Toast.LENGTH_LONG).show()
        }
    }

    private fun buildSecondSpeechRecognizer(language: String?) {
        initSecondTextToSpeech(language1)
        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, decideLanguage(language))
        sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
        try {
            startActivityForResult(sttIntent, LANGUAGE2CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "Your device does not support STT.", Toast.LENGTH_LONG).show()
        }
    }

    private fun firstToSecondLanguageSpeechModel(inputText: String) {

        val firstToSecondLanguageTranslator = Translation.getClient(languageOptions1(language1, language2))

        val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
        firstToSecondLanguageTranslator.downloadModelIfNeeded(conditions)
                            .addOnSuccessListener {
                    firstToSecondLanguageTranslator.translate(inputText)
                            .addOnSuccessListener {
                        languageDisplayTv.text = it
                                if (it.isNotEmpty()) {
                                    // Lollipop and above requires an additional ID to be passed.
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        // Call Lollipop+ function
                                        textToSpeech1?.speak(it, TextToSpeech.QUEUE_FLUSH, null, "tts1")
                                    } else {
                                        // Call Legacy function
                                        textToSpeech1?.speak(it, TextToSpeech.QUEUE_FLUSH, null)
                                    }
                                } else {
                                    Toast.makeText(this, "Text cannot be empty", Toast.LENGTH_LONG).show()
                                }
                    }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show()
                            }
                }
                            .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show()
                }
    }

    private fun secondToFirstLanguageSpeechModel(inputText: String) {

        val secondToFirstLanguageTranslator = Translation.getClient(languageOptions2(language2, language1))

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        secondToFirstLanguageTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                secondToFirstLanguageTranslator.translate(inputText)
                    .addOnSuccessListener {
                        languageDisplayTv.text = it
                        if (it.isNotEmpty()) {
                            // Lollipop and above requires an additional ID to be passed.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                // Call Lollipop+ function
                                textToSpeech2?.speak(it, TextToSpeech.QUEUE_FLUSH, null, "tts1")


                            } else {
                                // Call Legacy function
                                textToSpeech2?.speak(it, TextToSpeech.QUEUE_FLUSH, null)
                            }
                        } else {
                            Toast.makeText(this, "Text cannot be empty", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            LANGUAGE1CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (!result.isNullOrEmpty()) {
                        val recognizedText = result[0]
                        firstToSecondLanguageSpeechModel(recognizedText)
                    }
                }
            }

            LANGUAGE2CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (!result.isNullOrEmpty()) {
                        val recognizedText = result[0]
                        secondToFirstLanguageSpeechModel(recognizedText)
                    }
                }
            }
        }
    }



    override fun onPause() {
        textToSpeech1?.stop()
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeech1?.shutdown()
        super.onDestroy()
    }
}