package com.marcossalto.targetmvd.utils

import android.app.Activity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.marcossalto.targetmvd.network.managers.SessionManager
import com.marcossalto.targetmvd.network.managers.UserManager
import com.marcossalto.targetmvd.network.models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.Dispatcher
import org.hamcrest.CoreMatchers.anything
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
open class BaseTests {

    var mockServer: MockServer = MockServer
    val email: String = "bradley.cooper@mail.com"
    val password: String = "12345678"
    val username: String = "bradleycooper"
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    open fun setServerDispatch(dispatcher: Dispatcher) {
        mockServer.server().dispatcher = dispatcher
    }

    open fun before() {
        mockServer.startServer()
        UserManager.reloadService(mockServer.server().url("/").toString())
    }

    open fun after() {
        mockServer.stopServer()
    }

    open fun setupSession() {
        SessionManager.user = testUser()
    }

    open fun logout() {
        SessionManager.signOut()
    }

    open fun testUserSignIn() = UserSignIn(
        email = email,
        password = password
    )

    private val smallThumb : SmallThumb = SmallThumb(null)

    private val normal : Normal = Normal(null)

    private val avatar : Avatar = Avatar(
        url = null,
        normal = normal,
        smallThumb = smallThumb
    )

    private val user : User = User(
        id=1019,
        email=email,
        uid=email,
        provider="email",
        avatar,
        firstName="",
        lastName="",
        username=username,
        gender="male",
        pushToken=null,
        allowPasswordChange=false
    )

    open fun testUserSignInResponse() = UserSignInResponseSerializer(user=user)

    open fun testUser() = user
    
    open fun testUserSignUp() = UserSignUp(
        username=username,
        email=email,
        gender="male",
        password=password,
        passwordConfirmation=password
    )

    open fun scrollAndTypeText(id: Int, text: String) {
        onView(ViewMatchers.withId(id)).perform(
            ViewActions.scrollTo(),
            ViewActions.click(),
            ViewActions.clearText(),
            ViewActions.typeText(text),
            ViewActions.closeSoftKeyboard()
        )
    }

    open fun scrollAndSelectItem(spinnerId: Int, selectionText: String){
        onView(ViewMatchers.withId(spinnerId)).perform(ViewActions.click())
        onData(anything()).atPosition(1).perform(click());
    }

    open fun typeText(id: Int, text: String) {
        onView(ViewMatchers.withId(id)).perform(
            ViewActions.click(),
            ViewActions.clearText(),
            ViewActions.typeText(text),
            ViewActions.closeSoftKeyboard()
        )
    }

    open fun scrollAndPerformClick(viewId: Int) {
        onView(ViewMatchers.withId(viewId))
            .perform(ViewActions.scrollTo(), ViewActions.click())
    }

    open fun performClick(viewId: Int) {
        onView(ViewMatchers.withId(viewId)).perform(ViewActions.click())
    }

    open fun stringMatches(viewId: Int, value: String) {
        onView(ViewMatchers.withId(viewId)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    value
                )
            )
        )
    }

    open fun currentActivity(): Activity {
        // Get the activity that currently started
        val activities =
            ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        return activities.first()
    }
}
