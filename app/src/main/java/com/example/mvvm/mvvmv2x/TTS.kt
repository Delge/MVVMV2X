package com.example.mvvm.mvvmv2x


import android.speech.tts.TextToSpeech
import java.util.*

class TTS: TextToSpeech.OnInitListener {
    lateinit var language : String

     val tts: TextToSpeech = TextToSpeech(MyApplication.context,this)
    override fun onInit(p0: Int) {
        if (p0 == TextToSpeech.SUCCESS){
            val locale =Locale.getDefault()
            language = locale.language
            if (language.endsWith("zh")){
                tts.language = Locale.SIMPLIFIED_CHINESE
            }else{
                tts.language = Locale.ENGLISH
            }
        }
    }
}