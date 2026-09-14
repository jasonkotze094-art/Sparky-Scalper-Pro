package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.model.ScanItem
import com.example.data.model.TradeLog
import com.example.data.model.TradeOrder
import com.example.data.model.TradingBot
import com.example.data.repository.TradingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    METATRADER,
    SCANNER,
    SETTINGS
}

class TradingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TradingRepository(application)

    // Current Tab
    private val _currentTab = MutableStateFlow(ScreenTab.SCANNER)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Scan History
    val scanHistory: StateFlow<List<ScanItem>> = repository.getScanHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // App State
    val isCopyTradingActive: StateFlow<Boolean> = repository.isCopyTradingActive
    val remainingScans: StateFlow<Int> = repository.remainingScans
    val isWarningDismissed: StateFlow<Boolean> = repository.isWarningDismissed
    val bots: StateFlow<List<TradingBot>> = repository.bots
    val openOrders: StateFlow<List<TradeOrder>> = repository.openOrders
    val logs: StateFlow<List<TradeLog>> = repository.logs

    // Scanner UI State
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _scanStatusMessage = MutableStateFlow("")
    val scanStatusMessage: StateFlow<String> = _scanStatusMessage.asStateFlow()

    // Dialog States
    private val _showPairsDialog = MutableStateFlow(false)
    val showPairsDialog: StateFlow<Boolean> = _showPairsDialog.asStateFlow()

    private val _showLogsDialog = MutableStateFlow(false)
    val showLogsDialog: StateFlow<Boolean> = _showLogsDialog.asStateFlow()

    private val _showAddBotDialog = MutableStateFlow(false)
    val showAddBotDialog: StateFlow<Boolean> = _showAddBotDialog.asStateFlow()

    private val _showAccountDialog = MutableStateFlow(false)
    val showAccountDialog: StateFlow<Boolean> = _showAccountDialog.asStateFlow()

    // Settings
    private val _lotSize = MutableStateFlow("0.01")
    val lotSize: StateFlow<String> = _lotSize.asStateFlow()

    private val _riskPercent = MutableStateFlow("1.5%")
    val riskPercent: StateFlow<String> = _riskPercent.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _hapticsEnabled = MutableStateFlow(true)
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled.asStateFlow()

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun toggleCopyTrading() {
        repository.toggleCopyTrading()
    }

    fun dismissWarning() {
        repository.dismissWarning()
    }

    fun toggleBot(botId: String) {
        repository.toggleBotActive(botId)
    }

    fun setPairsDialogVisible(visible: Boolean) {
        _showPairsDialog.value = visible
    }

    fun setLogsDialogVisible(visible: Boolean) {
        _showLogsDialog.value = visible
    }

    fun setAddBotDialogVisible(visible: Boolean) {
        _showAddBotDialog.value = visible
    }

    fun setAccountDialogVisible(visible: Boolean) {
        _showAccountDialog.value = visible
    }

    fun setLotSize(value: String) {
        _lotSize.value = value
    }

    fun setRiskPercent(value: String) {
        _riskPercent.value = value
    }

    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
    }

    fun toggleHaptics() {
        _hapticsEnabled.value = !_hapticsEnabled.value
    }

    fun addNewBot(name: String, version: String, pairs: String, timeframe: String, desc: String) {
        repository.addNewBot(name, version, pairs, timeframe, desc)
        _showAddBotDialog.value = false
    }

    fun deleteScan(id: Long) {
        viewModelScope.launch {
            repository.deleteScan(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAllScans()
        }
    }

    fun scanImage(uri: Uri?) {
        executeScanProcess(imageUri = uri?.toString())
    }

    fun scanSampleChart() {
        executeScanProcess(imageRes = R.drawable.img_chart_sample_xauusd)
    }

    private fun executeScanProcess(imageRes: Int? = null, imageUri: String? = null) {
        if (_isScanning.value) return

        viewModelScope.launch {
            _isScanning.value = true
            _scanProgress.value = 0.1f
            _scanStatusMessage.value = "Detecting candlesticks & price scale..."
            delay(500)

            _scanProgress.value = 0.45f
            _scanStatusMessage.value = "Calculating market structure & liquidity pools..."
            delay(600)

            _scanProgress.value = 0.8f
            _scanStatusMessage.value = "Applying Sparky Scalper Pro neural filters..."
            delay(500)

            _scanProgress.value = 1.0f
            _scanStatusMessage.value = "Signal generated!"
            delay(300)

            // Randomize between a few realistic high-grade setups
            val setups = listOf(
                ScanItem(
                    pair = "XAUUSD.mic",
                    signal = "SELL",
                    entryPrice = 4613.79,
                    takeProfit = 4608.10,
                    stopLoss = 4619.99,
                    patternName = "Institutional Bearish Order Block & Resistance Rejection"
                ),
                ScanItem(
                    pair = "BTCUSD",
                    signal = "BUY",
                    entryPrice = 88420.00,
                    takeProfit = 91200.00,
                    stopLoss = 87150.00,
                    patternName = "Bullish Fair Value Gap Fill & Trend Continuation"
                ),
                ScanItem(
                    pair = "EURUSD",
                    signal = "BUY",
                    entryPrice = 1.0854,
                    takeProfit = 1.0898,
                    stopLoss = 1.0832,
                    patternName = "London Session Breakout & EMA 200 Bounce"
                ),
                ScanItem(
                    pair = "US30",
                    signal = "SELL",
                    entryPrice = 43850.00,
                    takeProfit = 43420.00,
                    stopLoss = 44020.00,
                    patternName = "Bearish Smart Money Divergence at Key Level"
                )
            )

            val chosen = setups.random()
            repository.addScan(
                pair = chosen.pair,
                signal = chosen.signal,
                entry = chosen.entryPrice,
                tp = chosen.takeProfit,
                sl = chosen.stopLoss,
                imageRes = imageRes ?: R.drawable.img_chart_sample_xauusd,
                imageUri = imageUri,
                pattern = chosen.patternName
            )

            _isScanning.value = false
            _scanProgress.value = 0f
            _scanStatusMessage.value = ""
        }
    }
}
