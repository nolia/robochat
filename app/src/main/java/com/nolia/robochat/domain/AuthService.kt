package com.nolia.robochat.domain

import io.reactivex.Completable
import io.reactivex.Single

interface AuthService {

    fun isUserRegistered(): Single<Boolean>

    fun sendPhoneNumber(phoneNumber: String): Single<String>

    fun sendCode(code: String): Completable

    fun logout(): Completable
}
