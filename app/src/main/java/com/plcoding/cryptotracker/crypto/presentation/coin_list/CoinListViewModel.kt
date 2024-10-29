package com.plcoding.cryptotracker.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptotracker.core.domain.CoinDataSource
import com.plcoding.cryptotracker.core.domain.util.onError
import com.plcoding.cryptotracker.core.domain.util.onSuccess
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.DataPoint
import com.plcoding.cryptotracker.crypto.presentation.models.CoinUi
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state.asStateFlow()

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadCoins()
    }

    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClicked -> {
                selectCoin(action.coinUi)
            }
        }
    }

    private fun selectCoin(coinUi: CoinUi) {
        _state.update { it.copy(selectedCoin = coinUi) }

        viewModelScope.launch {
            coinDataSource.getCoinHistory(
                coinId = coinUi.id,
                start = ZonedDateTime.now().minusDays(5),
                end = ZonedDateTime.now()
            ).onSuccess { history ->
                val dataPoints = history.sortedBy { it.dateTime }.map {
                        DataPoint(
                            x=it.dateTime.hour.toFloat(),
                            y=it.priceUsd.toFloat(),
                            xLabel=DateTimeFormatter.ofPattern("ha\nM/d").format(it.dateTime)
                        )
                    }
                _state.update { it.copy(selectedCoin = coinUi.copy(coinPriceHistory = dataPoints)) }
            }.onError {
                _events.send(CoinListEvent.Error(it))
            }
        }
    }

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            coinDataSource.getCoins().onSuccess { coins ->
                _state.update {
                    it.copy(isLoading = false, coins = coins.map { it.toCoinUi() })
                }

            }.onError { error ->
                _state.update { it.copy(isLoading = false) }
                _events.send(CoinListEvent.Error(error))
            }
        }
    }
}