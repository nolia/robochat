package com.nolia.robochat.robot

import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.nolia.robochat.di.Injectable
import com.nolia.robochat.di.inject
import com.nolia.robochat.fake.TestConfig
import com.nolia.robochat.ui.splash.SplashActivity
import org.junit.Rule

abstract class BaseRobotTest : Injectable {

    @get:Rule
    val activityRule = ActivityScenarioRule(SplashActivity::class.java)

    protected val testConfig: TestConfig by inject()

    protected fun given(options: TestConfig.() -> Unit) {
        testConfig.apply(options)
    }

}
