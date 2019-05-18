package com.nolia.robochat.ui.main

import com.nolia.robochat.base.BasePresenter
import com.nolia.robochat.base.BaseView
import com.nolia.robochat.base.WithError
import com.nolia.robochat.base.WithProgress
import com.nolia.robochat.di.inject
import com.nolia.robochat.domain.AuthService
import io.reactivex.Observable

interface MainView : BaseView, WithProgress, WithError {

    val clickLogoutEvent: Observable<Unit>

    fun restartSplash()

}

class MainPresenter : BasePresenter<MainView>() {

    private val authService: AuthService by inject()

    override fun onBind(view: MainView) {
        super.onBind(view)

        viewDisposable.addAll(
            view.clickLogoutEvent.subscribe {
                doLogout(view)
            }
        )
    }

    private fun doLogout(view: MainView) {
        viewDisposable.addAll(
            authService.logout()
                .subscribe {
                    view.restartSplash()
                }
        )


    }

}
