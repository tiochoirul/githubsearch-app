package com.example.githubuserapp.ui.main

import android.view.KeyEvent
import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.githubuserapp.R
import org.junit.Test

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val dummyUserLogin = "tiochoirul"

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun findById() {
        onView(withId(R.id.edtSearch)).perform(typeText(dummyUserLogin))
        onView(withId(R.id.edtSearch)).perform(pressKey(KeyEvent.KEYCODE_ENTER))

    }

    @Test
    fun favoriteList() {
        onView(withId(R.id.btn_list_favorite)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_list_favorite)).perform(click())
    }
}