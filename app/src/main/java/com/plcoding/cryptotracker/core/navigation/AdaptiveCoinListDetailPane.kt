package com.plcoding.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.cryptotracker.core.presentation.util.ObserveAsEvents
import com.plcoding.cryptotracker.core.presentation.util.toString
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListAction
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveCoinListDetailPane(
    modifier: Modifier = Modifier, vm: CoinListViewModel = koinViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    ObserveAsEvents(events = vm.events) { event ->
        when (event) {
            is CoinListEvent.Error -> {
                Toast.makeText(
                    context, event.error.toString(context), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator, listPane = {
            AnimatedPane {
                CoinListScreen(state = state, onAction = {
                    vm.onAction(it)
                    when (it) {
                        is CoinListAction.OnCoinClicked -> navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail
                        )
                    }
                })
            }
        }, detailPane ={
            AnimatedPane {
                CoinDetailScreen(state = state)
            }
        },
        modifier = modifier
    )
}
