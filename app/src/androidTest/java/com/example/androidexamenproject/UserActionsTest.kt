
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
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
class UserActionsTest {

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
    fun navigationToHomeScreen() {
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
    }

    @Test
    fun getHighestFloorPrice() {
        // Reuse steps from the first test
        navigationToHomeScreen()

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithContentDescription("ETH Logo")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // After navigating to the home screen, search for the "Avatar" content description and click it
        composeTestRule
            .onNodeWithText("Highest floor price")
            .assertExists("Highest floor price is not displayed")
            .performClick()

        //Find text mogCats and click it
        composeTestRule
            .onNodeWithText("MOGCATS")
            .assertExists("mogCats is not displayed")
            .performClick()

        composeTestRule.waitForIdle()
    }


}