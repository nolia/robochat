package com.nolia.robochat.fake

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import com.nolia.robochat.di.Injectable
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

abstract class IdlingService(
    indlingResName: String
) : Injectable {

    protected var indlingDelay = 500L
    private val idlingResource = CountingIdlingResource(indlingResName, true)

    init {
        IdlingRegistry
            .getInstance()
            .register(idlingResource)
    }

    protected fun Completable.withIdling(): Completable = this
        .delay(indlingDelay, TimeUnit.MILLISECONDS)
        .doOnSubscribe { idlingResource.increment() }
        .doAfterTerminate { idlingResource.decrement() }

    protected fun <T> Single<T>.withIdling(): Single<T> = this
        .delay(indlingDelay, TimeUnit.MILLISECONDS)
        .doOnSubscribe { idlingResource.increment() }
        .doAfterTerminate { idlingResource.decrement() }

}
