package com.arpitoth.gtracingcompanion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun authScreen_allowsCredentialInputAndActionButtons() {
        composeTestRule.onNodeWithTag("auth_email").assertIsDisplayed().performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("auth_password").assertIsDisplayed().performTextInput("strongpw")
        composeTestRule.onNodeWithTag("auth_register_button").assertIsDisplayed().performClick()
    }

    @Test
    fun authScreen_loginButtonIsReachable() {
        composeTestRule.onNodeWithTag("auth_login_button").assertIsDisplayed().performClick()
    }
}
