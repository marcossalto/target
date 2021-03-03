package com.marcossalto.targetmvd

import com.marcossalto.targetmvd.network.models.UserSignUp
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Test

class UserTest {
    private val loader = javaClass.classLoader!!
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Test
    fun parse() {
        val jsonString = String(loader.getResourceAsStream("user.json").readBytes())
        val actual = moshi.adapter(UserSignUp::class.java).fromJson(jsonString)
        val expected = UserSignUp(
            "bradleycooper",
            "bradley.cooper@mail.com",
            "male",
            "12345678",
            "12345678"
        )
        assertEquals(expected, actual)
    }
}
