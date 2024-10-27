package com.plcoding.cryptotracker.data.mappers

import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.data.networking.dto.CoinDto

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        rank = rank,
        name = name,
        symbol = symbol,
        marketCapUsd = marketCapUsd,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr
    )
}