package com.example.mvvm.mvvmv2x

import android.graphics.drawable.Drawable
import android.provider.ContactsContract
import android.renderscript.Sampler
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(countReserved: Int) :ViewModel() {
    val counter : LiveData<Int>
        get() = _counter
    val ll_radio_visibility : LiveData<Int>
        get() = _ll_radio_visibility
    val tv_voice : LiveData<String>
        get() = _tv_voice
    val icon_voice : LiveData<Int>
        get() = _icon_voice
    val tv_time : LiveData<String>
        get() = _tv_time

    private val _counter = MutableLiveData<Int>()
    private val _ll_radio_visibility = MutableLiveData<Int>()
    private val _tv_voice = MutableLiveData<String>()
    private val _icon_voice = MutableLiveData<Int>()
    private val _tv_time = MutableLiveData<String>()
    lateinit var date : Date

    object Obj{
        @BindingAdapter("app:imgUrl")
        @JvmStatic fun load(imageView: ImageView, res: Int) {
            imageView.setImageResource(res)
        }
    }


    init {
        _counter.value = countReserved
        _ll_radio_visibility.value = View.GONE
        _tv_voice.value = "简洁"
        _icon_voice.value =  R.mipmap.icon_simple
    }

//    fun plusOne(){
//        val count = _counter.value ?:0
//        _counter.value = count + 1
//    }
//
//    fun clear(){
//        _counter.value = 0
//    }
    fun ClickTitle(){
    _ll_radio_visibility.value = View.VISIBLE
}
    fun ClickCancle(){
        _ll_radio_visibility.value = View.GONE
    }

    fun ClickConfirm(){
        _ll_radio_visibility.value = View.GONE
    }

    fun ClickVoice(){
        if (_tv_voice.value == "简洁"){
            _tv_voice.value = "静音"
            _icon_voice.value = R.mipmap.icon_mute
        }
            else if (_tv_voice.value == "静音"){
            _tv_voice.value = "详细"
            _icon_voice.value = R.mipmap.icon_voice
        }
            else if(_tv_voice.value == "详细"){
            _tv_voice.value = "简洁"
            _icon_voice.value = R.mipmap.icon_simple
        }
    }
    fun initView(){
        var sdf = SimpleDateFormat("HH:mm:ss")

    }
    fun initTts(){TTS()}

    fun  startThread(){}
    fun  stopVoice(){}
    private val mRunnable = Runnable {
//        date.time = System.currentTimeMillis()
//        val str_time = sdf.format(date)
//        _tv_time.value = str_time
    }

}
