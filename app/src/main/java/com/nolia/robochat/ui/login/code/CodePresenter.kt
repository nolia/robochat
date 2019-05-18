package com.nolia.robochat.ui.login.code

import com.nolia.robochat.base.BasePresenter
import com.nolia.robochat.base.BaseView
import com.nolia.robochat.base.WithError
import com.nolia.robochat.base.WithProgress
import com.nolia.robochat.di.inject
import com.nolia.robochat.domain.AuthService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers


interface CodeView : BaseView, WithError, WithProgress {

    val submitCodeEvent: Observable<String>

    fun openMain()
}

class CodePresenter : BasePresenter<CodeView>() {

    private val authService: AuthService by inject()

    override fun onBind(view: CodeView) {
        super.onBind(view)

        viewDisposable.addAll(
            view.submitCodeEvent.subscribe {
                sendCode(it, view)
            }
        )
    }

    private fun sendCode(code: String, view: CodeView) {
        viewDisposable.add(
            authService.sendCode(code)
                .observeOn(AndroidSchedulers.mainThread())
                .withErrorAndProgress()
                .subscribe({
                    view.openMain()
                }, {})
        )
    }

}
