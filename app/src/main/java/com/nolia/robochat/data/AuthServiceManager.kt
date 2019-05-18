package com.nolia.robochat.data

import android.annotation.SuppressLint
import android.content.Context
import com.nolia.robochat.domain.AuthService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private const val USER_KEY = "user-key"

open class AuthServiceManager(context: Context) : AuthService {

    private val prefs = context.getSharedPreferences("fake_auth", Context.MODE_PRIVATE)

    override fun isUserRegistered(): Single<Boolean> = Single.fromCallable {
        prefs.contains(USER_KEY)
    }.delay(1, TimeUnit.SECONDS, Schedulers.computation(), true)

    @SuppressLint("ApplySharedPref")
    override fun logout(): Completable = Completable.fromAction {
        prefs.edit()
            .remove(USER_KEY)
            .commit()
    }

    override fun sendPhoneNumber(phoneNumber: String): Single<String> = Single.fromCallable {
        "1111"
    }.delay(1, TimeUnit.SECONDS, Schedulers.computation(), true)


    override fun sendCode(code: String): Completable = Completable.fromAction {
        if (code != "1111") throw IllegalArgumentException("Invalid code!")

        prefs.edit()
            .putString(USER_KEY, "1111")
            .apply()
    }
        .delay(1, TimeUnit.SECONDS, Schedulers.computation(), true)


}
