package com.nolia.robochat.robot

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.nolia.robochat.di.Injectable
import com.nolia.robochat.di.get
import com.nolia.robochat.di.inject
import com.nolia.robochat.fake.TestConfig
import com.nolia.robochat.ui.splash.SplashActivity
import org.junit.Before
import org.junit.Rule

abstract class BaseRobotTest : Injectable {

    lateinit var activityScenario: ActivityScenario<SplashActivity>

    @Before
    fun setUp() {
        beforeActivity()
        activityScenario = ActivityScenario.launch(SplashActivity::class.java)
    }

    protected open fun beforeActivity() {
        // Left for rent.
    }

    protected val testConfig: TestConfig by inject()

    protected fun given(options: TestConfig.() -> Unit) {
        testConfig.apply(options)
    }

    protected inline fun <reified Service> givenService(options: Service.() -> Unit) {
        get<Service>().apply(options)
    }

}
