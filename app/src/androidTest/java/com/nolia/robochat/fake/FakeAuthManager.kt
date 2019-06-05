package com.nolia.robochat.fake

import com.nolia.robochat.data.AuthServiceManager
import com.nolia.robochat.di.inject
import com.nolia.robochat.domain.AuthService
import io.reactivex.Completable
import io.reactivex.Single

// In reality, the real AuthService would call a real service
// while fake would be using test config and not sending anywhere.
class FakeAuthManager(
    private val realInstance: AuthServiceManager
) :
    IdlingService("fake_auth"),
    AuthService by realInstance {

    private val testConfig: TestConfig by inject()

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

    override fun logout(): Completable = realInstance
        .logout()
        .withIdling()

    override fun sendCode(code: String): Completable = realInstance
        .sendCode(code)
        .withIdling()

}
