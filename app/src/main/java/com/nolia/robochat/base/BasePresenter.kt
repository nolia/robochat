package com.nolia.robochat.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.nolia.robochat.di.Injectable
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

interface BaseView

interface WithError {
    fun showError(error: String) {}
}

interface WithProgress {
    fun showProgress(progress: Boolean) {}
}

abstract class BasePresenter<View : BaseView> : Injectable, LifecycleObserver {

    private var view: View? = null
    protected val viewDisposable = CompositeDisposable()

    fun bind(view: View) {
        this.view = view
        onBind(view)

        if (view is LifecycleOwner) {
            view.lifecycle.addObserver(this)
        }
    }

    protected open fun onBind(view: View) {}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unbind() {
        viewDisposable.clear()
        view = null
    }

    /**
     *  Call this after `observeOn(mainThread())`.
     */
    protected fun <T> Maybe<T>.withErrorAndProgress(): Maybe<T> {
        val view = view ?: return this

        var maybe = this
        if (view is WithProgress) {
            maybe = maybe
                .doOnSubscribe { view.showProgress(true) }
                .doAfterTerminate { view.showProgress(false) }

        }

        if (view is WithError) {
            maybe = maybe
                .doOnError { t -> view.showError(t.localizedMessage ?: "Error!") }
        }

        return maybe
    }

    /**
     *  Call this after `observeOn(mainThread())`.
     */
    protected fun <T> Single<T>.withErrorAndProgress(): Single<T> {
        val view = view ?: return this

        var maybe = this
        if (view is WithProgress) {
            maybe = maybe
                .doOnSubscribe { view.showProgress(true) }
                .doAfterTerminate { view.showProgress(false) }

        }

        if (view is WithError) {
            maybe = maybe
                .doOnError { t -> view.showError(t.localizedMessage ?: "Error!") }
        }

        return maybe
    }

    protected fun Completable.withErrorAndProgress(): Completable {
        val view = view ?: return this

        var maybe = this
        if (view is WithProgress) {
            maybe = maybe
                .doOnSubscribe { view.showProgress(true) }
                .doAfterTerminate { view.showProgress(false) }

        }

        if (view is WithError) {
            maybe = maybe
                .doOnError { t -> view.showError(t.localizedMessage ?: "Error!") }
        }

        return maybe
    }
}
