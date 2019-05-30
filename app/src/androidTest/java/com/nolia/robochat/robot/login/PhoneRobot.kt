package com.nolia.robochat.robot.login

import com.nolia.robochat.R
import com.nolia.robochat.robot.BaseRobot

fun phone(actions: PhoneRobot.() -> Unit) = PhoneRobot().apply(actions)

class PhoneRobot : BaseRobot() {

    init {
        // Implicit assertion to make sure we are on the right screen.
        textIsVisible(R.string.phone_label)
    }

    fun inputPhoneNumber(phoneNumber: String) {
        typeInText(R.id.phoneEditText, phoneNumber)
    }

    fun clickSubmit() {
        clickOnId(R.id.nextButton)
    }

    fun errorIsVisible() {
        viewIsVisible(R.id.errorTextView)
    }

    fun inputCodeTitleIsShown() {
        textIsVisible(R.string.code_label)
    }

}
