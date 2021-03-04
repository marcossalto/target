package com.marcossalto.targetmvd.tests

import androidx.test.core.app.ActivityScenario
import com.google.gson.Gson
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.network.managers.SessionManager
import com.marcossalto.targetmvd.network.models.User
import com.marcossalto.targetmvd.network.models.UserSignInResponseSerializer
import com.marcossalto.targetmvd.ui.signin.SignInActivity
import com.marcossalto.targetmvd.ui.signup.SignUpActivity
import com.marcossalto.targetmvd.ui.target.TargetActivity
import com.marcossalto.targetmvd.utils.BaseTests
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SignUpActivityTest : BaseTests() {

    private lateinit var activity: SignUpActivity
    private lateinit var scenario: ActivityScenario<SignUpActivity>

    @Before
    override fun before() {
        super.before()
        logout()
        scenario = ActivityScenario.launch(SignUpActivity::class.java)
        scenario.onActivity { activity -> this.activity = activity }
    }

    @Test
    fun signUpSuccessfulTest() {
        scenario.recreate()
        setServerDispatch(signUpDispatcher())
        val testUserSignUp = testUserSignUp()
        val testUser = testUser()
        scrollAndTypeText(R.id.name_edit_text, testUserSignUp.username)
        scrollAndTypeText(R.id.email_edit_text, testUserSignUp.email)
        scrollAndTypeText(R.id.password_edit_text, testUserSignUp.password)
        scrollAndTypeText(R.id.confirm_password_edit_text, testUserSignUp.passwordConfirmation)
        scrollAndSelectItem(R.id.gender_spinner, testUserSignUp.gender)
        scrollAndPerformClick(R.id.sign_up_button_sign_up)
        val user = SessionManager.user
        assertEquals(testUser, user)
        activity.runOnUiThread {
            val current = currentActivity()
            assertEquals(TargetActivity::class.java.name, current::class.java.name)
        }
    }

    @Test
    fun checkCTASignIn() {
        scenario.recreate()
        scrollAndPerformClick(R.id.sign_in_button_sign_up)
        activity.runOnUiThread {
            val current = currentActivity()
            assertEquals(SignInActivity::class.java.name, current::class.java.name)
        }
    }

    private fun signUpDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return if (request.path!!.contains("users")) {
                    val user = moshi.adapter(User::class.java)
                        .toJson(testUser())
                    mockServer.successfulResponse().setBody(user)
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
