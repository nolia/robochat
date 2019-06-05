package com.nolia.robochat.robot

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

abstract class BaseRobot {

    init {
        Espresso.onIdle()
    }

    fun clickOnId(id: Int) {
        onView(withId(id)).perform(click())
    }

    fun clickOnText(text: String) {
        onView(withText(text)).perform(click())
    }

    fun clickOnText(textId: Int) {
        onView(withText(textId)).perform(click())
    }

    fun viewIsVisible(id: Int) {
        onView(withId(id)).check(matches(isDisplayed()))
    }

    fun textIsVisible(text: String) {
        onView(withText(text)).check(matches(isDisplayed()))
    }

    fun textIsVisible(textId: Int) {
        onView(withText(textId)).check(matches(isDisplayed()))
    }

    fun typeInText(id: Int, text: String) {
        onView(withId(id))
            .perform(clearText())
            .perform(typeText(text))

        Espresso.closeSoftKeyboard()
    }

    protected fun recyclerPosition(recyclerViewId: Int, pos: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>()  {
            override fun describeTo(description: Description?) {
                description?.appendText("At recycler pos: $pos")
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item == null) {
                    return false
                }

                val recyclerView = item.rootView.findViewById<RecyclerView>(recyclerViewId)
                recyclerView.scrollToPosition(pos)

                return recyclerView.findViewHolderForAdapterPosition(pos)?.itemView == item
            }

        }
    }
}
