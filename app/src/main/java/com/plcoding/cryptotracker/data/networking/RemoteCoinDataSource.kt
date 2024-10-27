import com.plcoding.core.data.networking.constructUrl
import com.plcoding.core.data.networking.safeCall
import com.plcoding.core.domain.CoinDataSource
import com.plcoding.core.domain.util.NetworkError
import com.plcoding.core.domain.util.Result
import com.plcoding.core.domain.util.map
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.data.mappers.toCoin
import com.plcoding.cryptotracker.data.networking.dto.CoinsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteCoinDataSource(
    private val httpClient: HttpClient
) : CoinDataSource {

    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }
}