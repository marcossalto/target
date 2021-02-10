package com.marcossalto.targetmvd

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        goToSignIn()
    }

    private fun goToSignIn() {
        val splash = object : Thread(){
            override fun run(){
                try{
                    sleep(1500)
                    val intent = Intent(this@SplashActivity, SignInActivity::class.java)
                    finish()
                    startActivity(intent)
                }catch (e: Exception){}
            }
        }
        splash.start()
    }
}