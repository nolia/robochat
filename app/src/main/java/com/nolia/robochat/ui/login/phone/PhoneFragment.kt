package com.nolia.robochat.ui.login.phone

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import com.nolia.robochat.R
import com.nolia.robochat.base.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_phone.*

class PhoneFragment : BaseFragment(R.layout.fragment_phone), PhoneView {

    interface Callback {
        fun openCodeScreen()
    }

    private val phonePresenter = PhonePresenter()
    private val phoneSubject = PublishSubject.create<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()

        phonePresenter.bind(this)
    }

    private fun setupViews() {
        phoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        nextButton.setOnClickListener {
            phoneSubject.onNext(phoneEditText.text.toString())
        }
    }

    override val submitPhoneNumberEvent: Observable<String> get() = phoneSubject

    override fun showProgress(progress: Boolean) {
        phoneEditText.isEnabled = !progress
        progressBar.visibility = if (progress) View.VISIBLE else View.INVISIBLE
        if (progress) {
            errorTextView.visibility = View.INVISIBLE
        }
    }

    override fun showError(error: String) {
        errorTextView.text = error
        errorTextView.visibility = View.VISIBLE
    }

    override fun openCodeScreen() {
        val callback = activity as? Callback
        callback?.openCodeScreen()
    }
}
