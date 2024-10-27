package com.plcoding.core.domain

import com.plcoding.core.domain.util.NetworkError
import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.core.domain.util.Result

interface CoinDataSource {

        suspend fun getCoins(): Result<List<Coin>, NetworkError>
}