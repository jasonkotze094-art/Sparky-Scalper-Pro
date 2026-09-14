package com.example.data.model

data class ScanItem(
    val id: Long = 0,
    val pair: String,
    val signal: String, // "SELL" or "BUY"
    val entryPrice: Double,
    val takeProfit: Double,
    val stopLoss: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val timeAgo: String = "Just now",
    val timeframe: String = "H4",
    val botName: String = "Sparky Scalper Pro V2.0",
    val confidence: Double = 94.8,
    val patternName: String = "Liquidity Sweep & Order Block Reversal",
    val chartImageRes: Int? = null,
    val chartImageUri: String? = null
)
