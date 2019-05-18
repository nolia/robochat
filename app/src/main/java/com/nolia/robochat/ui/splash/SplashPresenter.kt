package com.nolia.robochat.ui.splash

import com.nolia.robochat.base.BasePresenter
import com.nolia.robochat.base.BaseView
import com.nolia.robochat.di.inject
import com.nolia.robochat.domain.AuthService
import io.reactivex.android.schedulers.AndroidSchedulers

interface SplashView : BaseView {

    fun openLogin()

    fun openMain()

}

class SplashPresenter : BasePresenter<SplashView>() {

    private val authService: AuthService by inject()

    override fun onBind(view: SplashView) {
        super.onBind(view)

        viewDisposable.addAll(
            authService.isUserRegistered()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { registered ->
                    if (registered) {
                        view.openMain()
                    } else {
                        view.openLogin()
                    }
                }
        )
    }

}
