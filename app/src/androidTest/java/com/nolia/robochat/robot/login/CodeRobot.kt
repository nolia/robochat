package com.nolia.robochat.robot.login

import com.nolia.robochat.R
import com.nolia.robochat.robot.BaseRobot

fun code(actions: CodeRobot.() -> Unit) = CodeRobot().apply(actions)

class CodeRobot : BaseRobot() {

    init {
        textIsVisible(R.string.code_label)
    }

    fun inputCode(code: String) {
        typeInText(R.id.codeEditText, code)
    }

    fun clickSubmit() {
        clickOnId(R.id.nextButton)
    }

    fun errorIsDisplayed() {
        viewIsVisible(R.id.errorTextView)
    }
}
