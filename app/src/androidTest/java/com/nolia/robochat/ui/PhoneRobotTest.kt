package com.nolia.robochat.ui

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.nolia.robochat.robot.BaseRobotTest
import com.nolia.robochat.robot.login.code
import com.nolia.robochat.robot.login.phone
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class PhoneRobotTest : BaseRobotTest() {

    @Test
    fun shouldValidatePhoneNumber() {
        val number = "555-5555"
        given {
            isUserRegistered = false
            userPhoneNumber = number
            correctCode = "1111"
        }

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
