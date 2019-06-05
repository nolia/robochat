package com.nolia.robochat.robot.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.nolia.robochat.R
import com.nolia.robochat.robot.BaseRobot
import org.hamcrest.Matchers.allOf

fun mainRobot(options: MainRobot.() -> Unit): MainRobot =
    MainRobot().apply(options)

class MainRobot : BaseRobot() {

    fun chatItemShown(pos: Int, title: String, lastMessage: String) {
        onView(recyclerPosition(R.id.recyclerView, pos))
            .check(
                matches(
                    allOf(
                        isDisplayed(),
                        hasDescendant(withText(title)),
                        hasDescendant(withText(lastMessage))
                    )
                )
            )
    }

}
