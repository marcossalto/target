package com.marcossalto.targetmvd.tests

import androidx.test.core.app.ActivityScenario
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.network.managers.SessionManager
import com.marcossalto.targetmvd.network.models.UserSignInResponseSerializer
import com.marcossalto.targetmvd.ui.signin.SignInActivity
import com.marcossalto.targetmvd.ui.target.TargetActivity
import com.marcossalto.targetmvd.utils.BaseTests
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SignInActivityTest : BaseTests() {

    private lateinit var activity: SignInActivity
    private lateinit var scenario: ActivityScenario<SignInActivity>

    @Before
    override fun before() {
        super.before()
        scenario = ActivityScenario.launch(SignInActivity::class.java)
        scenario.onActivity { activity -> this.activity = activity }
    }

    @Test
    fun signInSuccessfulTest() {
        scenario.recreate()
        setServerDispatch(signInDispatcher())
        val testUserSignIn = testUserSignIn()
        val testUser = testUser()
        typeText(R.id.email_edit_text, testUserSignIn.email)
        typeText(R.id.password_edit_text, testUserSignIn.password)
        performClick(R.id.sign_in_button)
        val user = SessionManager.user
        assertEquals(testUser, user)

        // Check if this activity was successful launched
        activity.runOnUiThread {
            val current = currentActivity()
            assertEquals(TargetActivity::class.java.name, current::class.java.name)
        }
    }

    private fun signInDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return if (request.path!!.contains("users/sign_in")) {
                    val response = moshi.adapter(UserSignInResponseSerializer::class.java)
                        .toJson(testUserSignInResponse())
                    mockServer.successfulResponse().setBody(response)
                } else
                    mockServer.notFoundResponse()
            }
        }
    }

    @After
    override fun after() {
        super.after()
    }
}
