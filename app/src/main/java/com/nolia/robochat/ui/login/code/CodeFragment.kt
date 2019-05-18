package com.nolia.robochat.ui.login.code

import android.os.Bundle
import android.view.View
import com.nolia.robochat.R
import com.nolia.robochat.base.BaseFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_code.*

class CodeFragment : BaseFragment(R.layout.fragment_code), CodeView {

    interface Callback {
        fun openMain()
    }


    private val presenter = CodePresenter()
    private val codeSubject = PublishSubject.create<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        presenter.bind(this)
    }

    private fun setupViews() {
        nextButton.setOnClickListener {
            codeSubject.onNext(codeEditText.text.toString())
        }
    }

    override val submitCodeEvent: Observable<String> get() = codeSubject

    override fun showError(error: String) {
        errorTextView.text = error
        errorTextView.visibility = View.VISIBLE
    }

    override fun showProgress(progress: Boolean) {
        progressBar.visibility = if (progress) View.VISIBLE else View.INVISIBLE
        codeEditText.isEnabled = !progress
        nextButton.isEnabled = !progress

        if (progress) {
            errorTextView.visibility = View.INVISIBLE
        }
    }

    override fun openMain() {
        val callback = activity as? Callback
        callback?.openMain()
    }

}
