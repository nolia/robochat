package com.nolia.robochat.ui.splash

import android.content.Intent
import android.os.Bundle
import com.nolia.robochat.R
import com.nolia.robochat.base.BaseActivity
import com.nolia.robochat.ui.login.LoginActivity
import com.nolia.robochat.ui.main.MainActivity

class SplashActivity : BaseActivity(R.layout.activity_splash),
    SplashView {

    private val splashPresenter = SplashPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashPresenter.bind(this)
    }

    override fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
