package com.nolia.robochat

import android.app.Application
import android.content.Context
import com.nolia.robochat.data.AuthServiceManager
import com.nolia.robochat.data.MessageServiceManager
import com.nolia.robochat.di.ServiceLocator
import com.nolia.robochat.domain.AuthService
import com.nolia.robochat.domain.MessageService

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val context = this
        ServiceLocator.defineDependencies(context)
    }

    protected open fun ServiceLocator.defineDependencies(context: Context) {
        set<AuthService>(AuthServiceManager(context))
        set<MessageService>(MessageServiceManager(context))
    }

}
