package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.db.AppDatabase
import com.example.data.db.ScanEntity
import com.example.data.model.ScanItem
import com.example.data.model.TradeLog
import com.example.data.model.TradeOrder
import com.example.data.model.TradingBot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class TradingRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val scanDao = db.scanDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    // EA Copy Trading Run State
    private val _isCopyTradingActive = MutableStateFlow(false)
    val isCopyTradingActive: StateFlow<Boolean> = _isCopyTradingActive.asStateFlow()

    // Remaining Scans Count
    private val _remainingScans = MutableStateFlow(10)
    val remainingScans: StateFlow<Int> = _remainingScans.asStateFlow()

    // Alert Banner Dismissed State
    private val _isWarningDismissed = MutableStateFlow(false)
    val isWarningDismissed: StateFlow<Boolean> = _isWarningDismissed.asStateFlow()

    // Bots List
    private val _bots = MutableStateFlow(
        listOf(
            TradingBot(
                id = "bot_1",
                name = "Sparky Scalper Pro V2.0",
                version = "V2.0",
                isActive = true,
                pairs = "XAUUSD, EURUSD, GBPUSD",
                timeframe = "M5, M15, H1",
                winRate = 89.6,
                totalTrades = 428,
                profitUSD = 4618.50,
                description = "High-frequency AI order flow & liquidity sweep scalper."
            ),
            TradingBot(
                id = "bot_2",
                name = "Gold Hunter Alpha",
                version = "V1.4",
                isActive = true,
                pairs = "XAUUSD.mic",
                timeframe = "H4, D1",
                winRate = 92.3,
                totalTrades = 156,
                profitUSD = 2840.00,
                description = "Institutional swing and breakout structure detector."
            )
        )
    )
    val bots: StateFlow<List<TradingBot>> = _bots.asStateFlow()

    // Active Orders
    private val _openOrders = MutableStateFlow(
        listOf(
            TradeOrder(
                id = "ord_7823",
                symbol = "XAUUSD.mic",
                type = "SELL",
                lots = 0.10,
                openPrice = 4613.79,
                currentPrice = 4608.10,
                sl = 4619.99,
                tp = 4608.10,
                profit = 56.90,
                openTime = "12h ago"
            ),
            TradeOrder(
                id = "ord_7824",
                symbol = "EURUSD",
                type = "BUY",
                lots = 0.25,
                openPrice = 1.08420,
                currentPrice = 1.08630,
                sl = 1.08200,
                tp = 1.08950,
                profit = 52.50,
                openTime = "4h ago"
            )
        )
    )
    val openOrders: StateFlow<List<TradeOrder>> = _openOrders.asStateFlow()

    // Logs
    private val _logs = MutableStateFlow(
        listOf(
            TradeLog(
                id = "log_1",
                timestamp = "12:18:24",
                symbol = "SYSTEM",
                message = "EAConnect Engine connected to VaultMarkets-Live DC 2",
                type = "INFO"
            ),
            TradeLog(
                id = "log_2",
                timestamp = "12:10:05",
                symbol = "XAUUSD.mic",
                message = "Sparky Scalper Pro V2.0 generated SELL signal at 4613.79",
                type = "SIGNAL"
            ),
            TradeLog(
                id = "log_3",
                timestamp = "11:55:12",
                symbol = "EURUSD",
                message = "Take Profit target TP1 reached for +21.0 pips ($52.50)",
                type = "TRADE"
            )
        )
    )
    val logs: StateFlow<List<TradeLog>> = _logs.asStateFlow()

    init {
        // Seed default scan if database is empty
        scope.launch {
            // Check if we need to insert initial item matching video
            insertInitialDefaultScan()
        }
    }

    private suspend fun insertInitialDefaultScan() {
        val initialItem = ScanEntity(
            id = 1,
            pair = "XAUUSD.mic",
            signal = "SELL",
            entryPrice = 4613.79,
            takeProfit = 4608.10,
            stopLoss = 4619.99,
            timestamp = System.currentTimeMillis() - 12 * 3600 * 1000,
            timeAgo = "12h ago",
            timeframe = "H4",
            botName = "Sparky Scalper Pro V2.0",
            confidence = 96.2,
            patternName = "Institutional Bearish Order Block & Resistance Rejection",
            chartImageRes = R.drawable.img_chart_sample_xauusd,
            chartImageUri = null
        )
        scanDao.insertScan(initialItem)
    }

    fun getScanHistory(): Flow<List<ScanItem>> {
        return scanDao.getAllScans().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun addScan(
        pair: String,
        signal: String,
        entry: Double,
        tp: Double,
        sl: Double,
        imageRes: Int? = null,
        imageUri: String? = null,
        pattern: String = "Algorithmic Smart Money Divergence"
    ) {
        val entity = ScanEntity(
            pair = pair,
            signal = signal,
            entryPrice = entry,
            takeProfit = tp,
            stopLoss = sl,
            timestamp = System.currentTimeMillis(),
            timeAgo = "Just now",
            timeframe = "M15",
            botName = "Sparky Scalper Pro V2.0",
            confidence = (91..98).random() + 0.4,
            patternName = pattern,
            chartImageRes = imageRes,
            chartImageUri = imageUri
        )
        scanDao.insertScan(entity)

        // Deduct remaining scan token
        if (_remainingScans.value > 0) {
            _remainingScans.value -= 1
        }

        // Add to log
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val newLog = TradeLog(
            id = UUID.randomUUID().toString(),
            timestamp = timeStr,
            symbol = pair,
            message = "Chart scanned: $signal detected at $entry (TP: $tp, SL: $sl)",
            type = "SIGNAL"
        )
        _logs.value = listOf(newLog) + _logs.value
    }

    suspend fun deleteScan(id: Long) {
        scanDao.deleteScanById(id)
    }

    suspend fun clearAllScans() {
        scanDao.clearAll()
    }

    fun toggleCopyTrading() {
        _isCopyTradingActive.value = !_isCopyTradingActive.value
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val actionText = if (_isCopyTradingActive.value) "STARTED" else "STOPPED"
        val log = TradeLog(
            id = UUID.randomUUID().toString(),
            timestamp = timeStr,
            symbol = "EAConnect",
            message = "Copy trading execution $actionText by user",
            type = if (_isCopyTradingActive.value) "TRADE" else "WARN"
        )
        _logs.value = listOf(log) + _logs.value
    }

    fun dismissWarning() {
        _isWarningDismissed.value = true
    }

    fun toggleBotActive(botId: String) {
        _bots.value = _bots.value.map {
            if (it.id == botId) it.copy(isActive = !it.isActive) else it
        }
    }

    fun addNewBot(name: String, version: String, pairs: String, timeframe: String, description: String) {
        val newBot = TradingBot(
            id = UUID.randomUUID().toString(),
            name = name,
            version = version,
            isActive = true,
            pairs = pairs,
            timeframe = timeframe,
            winRate = 91.2,
            totalTrades = 0,
            profitUSD = 0.0,
            description = description
        )
        _bots.value = _bots.value + newBot
    }

    fun resetTokens() {
        _remainingScans.value = 10
    }
}
