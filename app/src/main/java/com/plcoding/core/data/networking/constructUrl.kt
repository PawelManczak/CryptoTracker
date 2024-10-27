package com.plcoding.core.data.networking

import com.plcoding.cryptotracker.BuildConfig

fun constructUrl(url: String): String {
    return when{
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL + url.substring(1)
        else -> BuildConfig.BASE_URL + url
    }
}