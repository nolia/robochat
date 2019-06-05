package com.nolia.robochat

import android.content.Context
import androidx.test.espresso.IdlingRegistry
import com.nolia.robochat.data.MessageServiceManager
import com.nolia.robochat.di.ServiceLocator
import com.nolia.robochat.domain.AuthService
import com.nolia.robochat.domain.MessageService
import com.nolia.robochat.fake.FakeAuthManager
import com.nolia.robochat.fake.TestConfig

class TestApp : App() {

    override fun ServiceLocator.defineDependencies(context: Context) {
        set(TestConfig())

        val fakeAuthManager = FakeAuthManager(context)
        IdlingRegistry.getInstance().register(fakeAuthManager.idlingResource)

        set(fakeAuthManager)
        set<AuthService>(fakeAuthManager)
        set<MessageService>(MessageServiceManager(context))
    }
}
