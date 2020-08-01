package com.example.mvvm.mvvmv2x

import model.SaveExceptionUtil
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm.mvvmv2x.databinding.ActivityMainBinding
import com.example.mvvm.mvvmv2x.databinding.ActivityMainBindingImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel : MainViewModel
    lateinit var sp : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crashHandler = SaveExceptionUtil.getInstance()
        crashHandler.init(this)

        intPer()

        if (Build.VERSION.SDK_INT >= 21){
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        supportActionBar?.hide()

        setContentView(R.layout.activity_main)




        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved",0)

        var mainBingding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        viewModel= ViewModelProviders.of(this,MainViewModelFactory(countReserved)).get(MainViewModel::class.java)



        mainBingding.mainViewModel = viewModel
        mainBingding.lifecycleOwner =this



//        viewModel.counter.observe(this, Observer { count ->
//            infoText.text = count.toString()
//        })

        val animation = AnimationUtils.loadAnimation(this,R.anim.breath)
        viewModel.initView()
        viewModel.initTts()
        viewModel.startThread()
        viewModel.stopVoice()

        Log.d("test","END")





    }
    override fun onPause() {
        super.onPause()
        sp.edit(){
            putInt("count_reserved",viewModel.counter.value ?:0)
        }
    }
    private fun intPer(){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PERMISSION_GRANTED
        ) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
//           if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                   android.Manifest.permission.BYDAUTO_BODYWORK_COMMON)) {
//               Toast.makeText(BydApplication.mContext, "之前拒绝了该权限申请，需要清除应用数据，重新申请，否则不能使用！", Toast.LENGTH_SHORT).show();
//           } else {
            //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        }

    }

    override fun onResume() {
        if(requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        super.onResume()
    }

}