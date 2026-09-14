package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ScanItem

@Entity(tableName = "scan_history")
data class ScanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pair: String,
    val signal: String,
    val entryPrice: Double,
    val takeProfit: Double,
    val stopLoss: Double,
    val timestamp: Long,
    val timeAgo: String,
    val timeframe: String,
    val botName: String,
    val confidence: Double,
    val patternName: String,
    val chartImageRes: Int?,
    val chartImageUri: String?
) {
    fun toDomainModel(): ScanItem {
        return ScanItem(
            id = id,
            pair = pair,
            signal = signal,
            entryPrice = entryPrice,
            takeProfit = takeProfit,
            stopLoss = stopLoss,
            timestamp = timestamp,
            timeAgo = timeAgo,
            timeframe = timeframe,
            botName = botName,
            confidence = confidence,
            patternName = patternName,
            chartImageRes = chartImageRes,
            chartImageUri = chartImageUri
        )
    }

    companion object {
        fun fromDomainModel(item: ScanItem): ScanEntity {
            return ScanEntity(
                id = item.id,
                pair = item.pair,
                signal = item.signal,
                entryPrice = item.entryPrice,
                takeProfit = item.takeProfit,
                stopLoss = item.stopLoss,
                timestamp = item.timestamp,
                timeAgo = item.timeAgo,
                timeframe = item.timeframe,
                botName = item.botName,
                confidence = item.confidence,
                patternName = item.patternName,
                chartImageRes = item.chartImageRes,
                chartImageUri = item.chartImageUri
            )
        }
    }
}
