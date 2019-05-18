package com.nolia.robochat.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.nolia.robochat.R
import com.nolia.robochat.base.BaseActivity
import com.nolia.robochat.ui.splash.SplashActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main), MainView {

    private val clickLogoutSubject: PublishSubject<Unit> = PublishSubject.create()

    private val mainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        mainPresenter.bind(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu ?: return super.onCreateOptionsMenu(menu)

        val item = menu.add(R.string.logout)
        item.setOnMenuItemClickListener {
            clickLogoutSubject.onNext(Unit)
            true
        }

        return true
    }


    override val clickLogoutEvent: Observable<Unit>
        get() = clickLogoutSubject

    override fun restartSplash() {
        startActivity(
            Intent(this, SplashActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
