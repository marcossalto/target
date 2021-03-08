package com.marcossalto.targetmvd.ui.splash

import android.os.Bundle
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.network.managers.SessionManager.isUserSignedIn
import com.marcossalto.targetmvd.ui.base.BaseActivity
import com.marcossalto.targetmvd.ui.signin.SignInActivity
import com.marcossalto.targetmvd.ui.target.TargetActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        GlobalScope.launch(Dispatchers.Main) {
            initView()
        }
    }

    private suspend fun initView() {
        delay(1000)
        if (isUserSignedIn())
            startActivityClearTask(TargetActivity())
        else
            startActivityClearTask(SignInActivity())
    }
}