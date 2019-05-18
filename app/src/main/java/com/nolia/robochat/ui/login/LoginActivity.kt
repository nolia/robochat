package com.nolia.robochat.ui.login

import android.content.Intent
import android.os.Bundle
import com.nolia.robochat.R
import com.nolia.robochat.base.BaseActivity
import com.nolia.robochat.ui.login.code.CodeFragment
import com.nolia.robochat.ui.login.phone.PhoneFragment
import com.nolia.robochat.ui.main.MainActivity

class LoginActivity : BaseActivity(R.layout.activity_login),
    PhoneFragment.Callback,
    CodeFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, PhoneFragment())
            .commit()
    }

    override fun openCodeScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CodeFragment())
            .addToBackStack("phone")
            .commit()
    }

    override fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
