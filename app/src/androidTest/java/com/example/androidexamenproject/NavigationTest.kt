
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androidexamenproject.data.AlchemyRepository
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.ui.NFTApp
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kethereum.rpc.EthereumRPC
import org.mockito.kotlin.mock

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController

    private var localRepository: LocalRepository = mock()
    private var alchemyRepository: AlchemyRepository = mock()
    private var ethereumRPC: EthereumRPC = mock()

    private var alchemyViewModel: AlchemyViewModel = AlchemyViewModel(localRepository = localRepository, alchemyRepository = alchemyRepository, ethereumRPC = ethereumRPC)

    @Before
    fun setUp() {
        navController = TestNavHostController(
            context = ApplicationProvider.getApplicationContext()
        )
        composeTestRule.setContent {
            NFTApp()
        }
    }

    @Test
    fun testNavigationToHomeScreen() {
        composeTestRule
            .onNodeWithText("Address")
            .assertExists("Address TextField is not displayed")
            .performTextInput("bonko.eth")

        // Click on button with text "Start"
        composeTestRule
            .onNodeWithText("Start")
            .assertExists("Start button is not displayed")
            .performClick()

        // Wait for the UI to stabilize
        composeTestRule.waitForIdle()

        // Assert that the "NFT Collections" text is displayed
        composeTestRule
            .onNodeWithText("NFT Collections")
            .assertIsDisplayed()
    }

    @Test
    fun testNavigationToProfile() {
        // Reuse steps from the first test
        testNavigationToHomeScreen()

        // After navigating to the home screen, search for the "Avatar" content description and click it
        composeTestRule
            .onNodeWithContentDescription("Avatar")
            .assertExists("Avatar icon was not found")
            .performClick()
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Profile")
            .assertIsDisplayed()
    }

    @Test
    fun testNavigationToNftCollectionDetail() {
        // Navigate to the home screen
        testNavigationToHomeScreen()

        // Wait for the data to load by repeatedly checking for the "ETH Logo" content description
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithContentDescription("ETH Logo")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Select and interact with the first "ETH Logo" node
        composeTestRule
            .onAllNodesWithContentDescription("ETH Logo")
            .onFirst()  // Selects the first node
            .assertExists("ETH Logo was not found")
            .performClick()
    }


}