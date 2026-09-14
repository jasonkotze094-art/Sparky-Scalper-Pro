package com.example.data.model

data class TradingBot(
    val id: String,
    val name: String,
    val version: String,
    val isActive: Boolean,
    val pairs: String,
    val timeframe: String,
    val winRate: Double,
    val totalTrades: Int,
    val profitUSD: Double,
    val description: String
)

data class TradeOrder(
    val id: String,
    val symbol: String,
    val type: String, // "BUY" or "SELL"
    val lots: Double,
    val openPrice: Double,
    val currentPrice: Double,
    val sl: Double,
    val tp: Double,
    val profit: Double,
    val openTime: String
)

data class TradeLog(
    val id: String,
    val timestamp: String,
    val symbol: String,
    val message: String,
    val type: String // "INFO", "TRADE", "SIGNAL", "WARN"
)
