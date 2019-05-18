package com.nolia.robochat.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.nolia.robochat.R
import com.nolia.robochat.di.Injectable
import com.nolia.robochat.di.inject
import com.nolia.robochat.fake.TestConfig
import com.nolia.robochat.ui.splash.SplashActivity
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class OnboardingTest : Injectable {

    private val testConfig: TestConfig by inject()

    @Before
    fun setUp() {
        ActivityScenario.launch(SplashActivity::class.java)
    }

    @Test
    fun shouldValidatePhone() {
        val number = "555-5555"
        val errorMessage = "Invalid number!"

        testConfig.userPhoneNumber = number

        onView(withId(R.id.phoneEditText))
            .perform(typeText("4939393"))

        Espresso.closeSoftKeyboard()
        onView(withId(R.id.nextButton)).perform(click())

        onView(withText(errorMessage)).check(matches(isDisplayed()))

        onView(withId(R.id.phoneEditText))
            .perform(clearText())
            .perform(typeText(number))

        Espresso.closeSoftKeyboard()

        onView(withId(R.id.nextButton)).perform(click())

        onView(withText(R.string.code_label)).check(matches(isDisplayed()))
    }
}
