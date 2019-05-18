package com.nolia.robochat.fake

import android.content.Context
import androidx.test.espresso.idling.CountingIdlingResource
import com.nolia.robochat.data.AuthServiceManager
import com.nolia.robochat.di.Injectable
import com.nolia.robochat.di.inject
import io.reactivex.Completable
import io.reactivex.Single
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

// In reality, the real AuthService would call a real service
// while fake would be using test config and not sending anywhere.
class FakeAuthManager(context: Context) : AuthServiceManager(context), Injectable {

    val idlingResource = CountingIdlingResource("fake_auth", true)
    private val testConfig: TestConfig by inject()

    var delayMillis = 500L

    override fun isUserRegistered(): Single<Boolean> = Single
        .fromCallable { testConfig.isUserRegistered }
        .withIdling()

    override fun sendPhoneNumber(phoneNumber: String): Single<String> = Single
        .fromCallable {
            if (phoneNumber != testConfig.userPhoneNumber) {
                throw IllegalArgumentException("Invalid number!")
            }
            testConfig.correctCode
        }
        .withIdling()

    override fun logout(): Completable {
        return super.logout().withIdling()
    }

    override fun sendCode(code: String): Completable {
        return super.sendCode(code).withIdling()
    }

    private fun Completable.withIdling(): Completable = this
        .delay(delayMillis, TimeUnit.MILLISECONDS)
        .doOnSubscribe { idlingResource.increment() }
        .doAfterTerminate { idlingResource.decrement() }

    private fun <T> Single<T>.withIdling(): Single<T> = this
        .delay(delayMillis, TimeUnit.MILLISECONDS)
        .doOnSubscribe { idlingResource.increment() }
        .doAfterTerminate { idlingResource.decrement() }
}
