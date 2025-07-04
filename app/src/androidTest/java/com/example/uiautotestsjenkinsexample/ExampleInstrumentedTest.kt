package com.example.uiautotestsjenkinsexample

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// 📦 Basic app context test
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.uiautotestsjenkinsexample", appContext.packageName)
    }
}

// 🧪 Tests for MainActivity (Login)
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testInitialViewsDisplayed() {
        onView(withId(R.id.username)).check(matches(isDisplayed()))
        onView(withId(R.id.password)).check(matches(isDisplayed()))
        onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        onView(withId(R.id.error_text)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testLoginWithEmptyFieldsShowsError() {
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.error_text)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun testOnlyUsernameEnteredShowsError() {
        onView(withId(R.id.username)).perform(typeText("onlyUser"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.error_text)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun testWhitespaceInputsShowError() {
        onView(withId(R.id.username)).perform(typeText("   "), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("   "), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withId(R.id.error_text)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun testLoginWithValidCredentialsNavigatesToWelcomeActivity() {
        onView(withId(R.id.username)).perform(typeText("user"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("pass"), closeSoftKeyboard())
        onView(withId(R.id.login_button)).perform(click())
        onView(withText("Welcome, user!")).check(matches(isDisplayed()))
    }
}

// 🧪 Tests for WelcomeActivity (after Login)
@RunWith(AndroidJUnit4::class)
class WelcomeActivityTest {

    @Test
    fun testWelcomeViewsVisible() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), WelcomeActivity::class.java)
        intent.putExtra("username", "TestUser")
        val scenario = ActivityScenario.launch<WelcomeActivity>(intent)

        onView(withText("Welcome, TestUser!")).check(matches(isDisplayed()))
        onView(withId(R.id.logout_button)).check(matches(isDisplayed()))
    }

    @Test
    fun testLogoutButtonNavigatesBackToLogin() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), WelcomeActivity::class.java)
        intent.putExtra("username", "LoggedInUser")
        val scenario = ActivityScenario.launch<WelcomeActivity>(intent)

        onView(withId(R.id.logout_button)).perform(click())
        onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        onView(withId(R.id.username)).check(matches(isDisplayed()))
    }
}