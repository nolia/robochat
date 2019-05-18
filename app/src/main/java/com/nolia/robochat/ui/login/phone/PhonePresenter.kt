package com.nolia.robochat.ui.login.phone

import com.nolia.robochat.base.BasePresenter
import com.nolia.robochat.base.BaseView
import com.nolia.robochat.base.WithError
import com.nolia.robochat.base.WithProgress
import com.nolia.robochat.di.inject
import com.nolia.robochat.domain.AuthService
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

interface PhoneView : BaseView, WithError, WithProgress {
    val submitPhoneNumberEvent: Observable<String>

    fun openCodeScreen()
}

class PhonePresenter : BasePresenter<PhoneView>() {

    private val authService: AuthService by inject()

    override fun onBind(view: PhoneView) {
        super.onBind(view)

        viewDisposable.addAll(
            view.submitPhoneNumberEvent
                .flatMapMaybe { code -> sendNumberAndProcess(code, view) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun sendNumberAndProcess(
        phoneNumber: String,
        view: PhoneView
    ): Maybe<String> = authService.sendPhoneNumber(phoneNumber)
        .toMaybe()
        .observeOn(AndroidSchedulers.mainThread())
        .withErrorAndProgress()
        .doOnSuccess { view.openCodeScreen() }
        .onErrorComplete()
}
