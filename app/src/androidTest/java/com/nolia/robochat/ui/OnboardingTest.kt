package com.nolia.robochat.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nolia.robochat.robot.BaseRobotTest
import com.nolia.robochat.robot.login.code
import com.nolia.robochat.robot.login.phone
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnboardingTest : BaseRobotTest() {

    private val number = "555-5555"

    override fun beforeActivity() {
        given {
            isUserRegistered = false
            userPhoneNumber = number
            correctCode = "1111"
        }
    }

    @Test
    fun shouldValidatePhoneNumber() {
        phone {
            inputPhoneNumber("1111")
            clickSubmit()

            errorIsVisible()

            inputPhoneNumber(number)
            clickSubmit()
        }

        code {
            inputCode("1234")
            clickSubmit()

            errorIsDisplayed()

            inputCode("1111")
            clickSubmit()
        }
    }
}
