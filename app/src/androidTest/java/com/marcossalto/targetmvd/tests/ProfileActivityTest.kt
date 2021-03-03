package com.marcossalto.targetmvd.tests

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.network.managers.SessionManager
import com.marcossalto.targetmvd.ui.profile.ProfileActivity
import com.marcossalto.targetmvd.ui.signin.SignInActivity
import com.marcossalto.targetmvd.utils.BaseTests
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileActivityTest : BaseTests() {

    private lateinit var activity: ProfileActivity
    private lateinit var scenario: ActivityScenario<ProfileActivity>

    @Before
    override fun before() {
        super.before()
        setServerDispatch(logoutDispatcher())
        setupSession()
        scenario = ActivityScenario.launch(ProfileActivity::class.java)
        scenario.onActivity { activity -> this.activity = activity }
    }

    @Test
    fun profileUiTest() {
        SessionManager.user?.username?.let { stringMatches(R.id.user_name_edit_text, it) }
        SessionManager.user?.email?.let { stringMatches(R.id.email_edit_text, it) }

        onView(withId(R.id.logout_button)).perform(ViewActions.click())
        assertEquals(null, SessionManager.user)

        // Check if this activity was successful launched
        activity.runOnUiThread {
            val current = currentActivity()
            assertEquals(SignInActivity::class.java.name, current::class.java.name)
        }
    }

    private fun logoutDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return if (request.path!!.contains("users/sign_out"))
                    mockServer.successfulResponse()
                else
                    mockServer.notFoundResponse()
            }
        }
    }

    @After
    override fun after() {
        super.after()
    }
}
