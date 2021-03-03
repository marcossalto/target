package com.marcossalto.targetmvd

import com.marcossalto.targetmvd.util.extensions.isEmail
import org.junit.Assert
import org.junit.Test

class ValidationTests {
    @Test
    fun checkEmailTest() {
        Assert.assertEquals(true, "email@mail.com".isEmail())
        Assert.assertEquals(false, "email@mail".isEmail())
        Assert.assertEquals(false, "email".isEmail())
        Assert.assertEquals(false, "email.com".isEmail())
    }
}
