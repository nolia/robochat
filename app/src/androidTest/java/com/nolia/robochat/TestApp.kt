package com.nolia.robochat

import android.content.Context
import com.nolia.robochat.data.AuthServiceManager
import com.nolia.robochat.di.ServiceLocator
import com.nolia.robochat.domain.AuthService
import com.nolia.robochat.domain.MessageService
import com.nolia.robochat.fake.FakeAuthManager
import com.nolia.robochat.fake.FakeMessageService
import com.nolia.robochat.fake.TestConfig

class TestApp : App() {

    override fun ServiceLocator.defineDependencies(context: Context) {
        set(TestConfig())

        val realAuth = AuthServiceManager(context)
        val fakeAuthManager = FakeAuthManager(realAuth)

        set(fakeAuthManager)
        set<AuthService>(fakeAuthManager)

        val fakeMessageService = FakeMessageService()
        set(fakeMessageService)
        set<MessageService>(fakeMessageService)
    }
}
