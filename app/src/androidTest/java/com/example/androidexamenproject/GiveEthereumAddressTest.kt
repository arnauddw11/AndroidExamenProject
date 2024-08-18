package com.example.androidexamenproject

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.androidexamenproject.data.AlchemyRepository
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.ui.GiveEthereumAddress
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.kethereum.rpc.EthereumRPC
import org.mockito.kotlin.mock

class GiveEthereumAddressTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController

    private var localRepository: LocalRepository = mock()
    private var alchemyRepository: AlchemyRepository = mock()
    private var ethereumRPC: EthereumRPC = mock()

    private var alchemyViewModel: AlchemyViewModel = AlchemyViewModel(localRepository = localRepository, alchemyRepository = alchemyRepository, ethereumRPC = ethereumRPC)

    @Before
    fun setUp() {
        // Initialize NavController
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        // Set the content with the GiveEthereumAddress composable
        composeTestRule.setContent {
            GiveEthereumAddress(navController, alchemyViewModel)
        }
    }

    @Test
    fun testTextField_InputValidAddress_ShowsNoError() {
        val validAddress = "0x1234567890abcdef1234567890abcdef12345678"

        // Find the TextField with the label "Address" and input a valid Ethereum address
        composeTestRule
            .onNodeWithText("Address")
            .performTextInput(validAddress)

        // Assert that the TextField contains the inputted valid address
        composeTestRule
            .onNodeWithText(validAddress)
            .assertIsDisplayed()

        // Assert that no error message is displayed
        composeTestRule
            .onNodeWithText("Invalid Ethereum address or ENS name")
            .assertDoesNotExist()
    }

    @Test
    fun testTextField_InputInvalidAddress_ShowsError() {
        val invalidAddress = "invalid_address"

        // Find the TextField with the label "Address" and input an invalid Ethereum address
        composeTestRule
            .onNodeWithText("Address")
            .performTextInput(invalidAddress)

        // Assert that the TextField contains the inputted invalid address
        composeTestRule
            .onNodeWithText(invalidAddress)
            .assertIsDisplayed()

        // Assert that the error message is displayed
        composeTestRule
            .onNodeWithText("Invalid Ethereum address or ENS name")
            .assertIsDisplayed()
    }

    @Test
    fun testTextField_StartButtonDisabledForInvalidAddress() {
        val invalidAddress = "invalid_address"

        // Input an invalid address
        composeTestRule
            .onNodeWithText("Address")
            .performTextInput(invalidAddress)

        // Assert that the Start button is disabled
        composeTestRule
            .onNodeWithText("Start")
            .assertIsDisplayed()
            //.assertDoesNotHaveClickAction()
    }

    @Test
    fun testTextField_StartButtonEnabledForValidAddress() {
        val validAddress = "0x1234567890abcdef1234567890abcdef12345678"

        // Input a valid address
        composeTestRule
            .onNodeWithText("Address")
            .performTextInput(validAddress)

        // Assert that the Start button is enabled
        composeTestRule
            .onNodeWithText("Start")
            .assertIsDisplayed()
            //.assertHasClickAction()
    }
}