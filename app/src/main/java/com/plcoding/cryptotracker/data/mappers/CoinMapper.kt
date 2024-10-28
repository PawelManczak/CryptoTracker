package com.plcoding.cryptotracker.data.mappers

import com.plcoding.cryptotracker.crypto.domain.Coin
import com.plcoding.cryptotracker.crypto.domain.CoinPrice
import com.plcoding.cryptotracker.data.networking.dto.CoinDto
import com.plcoding.cryptotracker.data.networking.dto.CoinPriceDto
import java.time.Instant
import java.time.ZoneId

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

fun CoinPriceDto.toCoinPrice(): CoinPrice {
    return CoinPrice(
        priceUsd = priceUsd, dateTime = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault())
    )
}