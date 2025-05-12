package com.levurda.fitnessproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private lateinit var timer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        timer = object : CountDownTimer(2000,1000){
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }

        }.start()


      }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
    }
