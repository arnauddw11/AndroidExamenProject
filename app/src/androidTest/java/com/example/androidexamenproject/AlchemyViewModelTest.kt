
import android.util.Log
import com.example.androidexamenproject.data.AlchemyRepository
import com.example.androidexamenproject.data.LocalRepository
import com.example.androidexamenproject.data.NetworkAlchemyRepository
import com.example.androidexamenproject.data.OfflineLocalRepository
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.kethereum.rpc.EthereumRPC
import org.kethereum.rpc.HttpEthereumRPC
import org.mockito.kotlin.mock
private val ETH_NODE_URL = "https://eth-mainnet.g.alchemy.com/v2/XDJKhrYm6fHJodk3E0sXUOt_YpgePsdO"

class AlchemyViewModelTest {
    private val ETH_NODE_URL = "https://eth-mainnet.g.alchemy.com/v2/XDJKhrYm6fHJodk3E0sXUOt_YpgePsdO"
    private val localRepository: LocalRepository = OfflineLocalRepository(nftContractDao = mock(), ethereumAddressDao = mock())
    private val alchemyRepository: AlchemyRepository = NetworkAlchemyRepository(alchemyApiService = mock())
    private val ethereumRPC: EthereumRPC = HttpEthereumRPC(ETH_NODE_URL)
    private val viewModel = AlchemyViewModel(localRepository = localRepository, alchemyRepository = alchemyRepository, ethereumRPC = ethereumRPC)

    @Test
    fun alchemyViewModel_setEthAddress() = runBlocking {
        viewModel.setEthAddress("bonko.eth")
        viewModel.getEthereumAddress()


        // Wait until the ethereumAddress value is set
        val actualAddress = viewModel.ethereumAddress.value

        // Check if the address is set correctly
        assert(actualAddress == "0x123") { "Expected '0x123' but was '$actualAddress'" }
    }

    @Test
    fun alcemyViewModel_computeRarity() = runBlocking {
        Log.d("Rarities", viewModel.rarities.value.toString())
        assert(viewModel.rarities.value == null) { "Expected rarities to be null" }
        viewModel.computeRarity(
            contractAddress = "0x59CC849265e044116F95b69fef90C5091C8d0573", tokenId="211"
        )
        Log.d("Rarities", viewModel.rarities.value.toString())
        // assert rarities.value is not null
        assert(viewModel.rarities.value != null) { "Expected rarities to be non-null" }


    }
}
