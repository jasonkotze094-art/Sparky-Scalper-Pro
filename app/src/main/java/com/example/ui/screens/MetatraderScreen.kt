package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TradeOrder
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.GreenProfit
import com.example.ui.theme.RedPrimary
import com.example.ui.theme.RedSell
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.TradingViewModel

data class CandleData(
    val high: Float,
    val low: Float,
    val open: Float,
    val close: Float,
    val label: String
)

@Composable
fun MetatraderScreen(
    viewModel: TradingViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.openOrders.collectAsState()
    var selectedTimeframe by remember { mutableStateOf("H4") }
    var selectedSymbol by remember { mutableStateOf("XAUUSD.mic") }
    var crosshairY by remember { mutableStateOf<Float?>(null) }

    val timeframes = listOf("M1", "M5", "M15", "M30", "H1", "H4", "D1")
    val symbols = listOf("XAUUSD.mic", "EURUSD", "GBPUSD", "BTCUSD", "US30")

    // Candlesticks matching MT4 chart in video (purple bullish, black bearish)
    val candles = remember(selectedTimeframe, selectedSymbol) {
        listOf(
            CandleData(4582f, 4519f, 4522f, 4560f, "28 Apr 12:00"),
            CandleData(4565f, 4505f, 4560f, 4510f, ""),
            CandleData(4550f, 4500f, 4510f, 4545f, ""),
            CandleData(4615f, 4535f, 4545f, 4610f, ""),
            CandleData(4630f, 4580f, 4610f, 4625f, "29 Apr 20:00"),
            CandleData(4645f, 4590f, 4625f, 4600f, ""),
            CandleData(4610f, 4570f, 4600f, 4578f, ""),
            CandleData(4620f, 4560f, 4578f, 4615f, ""),
            CandleData(4675f, 4600f, 4615f, 4668f, "1 May 04:00"),
            CandleData(4660f, 4570f, 4668f, 4585f, ""),
            CandleData(4640f, 4575f, 4585f, 4635f, ""),
            CandleData(4635f, 4595f, 4635f, 4605f, ""),
            CandleData(4650f, 4560f, 4605f, 4565f, "4 May 08:00"),
            CandleData(4613.79f, 4560f, 4565f, 4608.10f, "Live")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // TOP ACCOUNT BAR (VaultMarkets Live DC 2 - Diego Moshe from video)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CyberSurface)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Diego Moshe",
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Connected",
                            tint = GreenProfit,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "5555737 - VaultMarkets-Live DC 2",
                        color = TextGrayMuted,
                        fontSize = 11.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Equity: $10,615.10",
                        color = GreenProfit,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Balance: $10,482.50",
                        color = TextGrayLight,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // SYMBOL SELECTOR ROW
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(symbols) { sym ->
                val isSelected = sym == selectedSymbol
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) RedPrimary else CyberSurface)
                        .clickable { selectedSymbol = sym }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = sym,
                        color = if (isSelected) TextWhite else TextGrayMuted,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // TIMEFRAMES BAR (M1, M5, M15, M30, H1, H4, D1 - matching MT4)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                timeframes.forEach { tf ->
                    val isSelected = tf == selectedTimeframe
                    Text(
                        text = tf,
                        color = if (isSelected) CyberCyan else TextGrayMuted,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier
                            .clickable { selectedTimeframe = tf }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            // Indicator tool icons
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crosshair",
                    tint = TextGrayMuted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Default.Functions,
                    contentDescription = "Indicators",
                    tint = TextGrayMuted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF1E2430))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("Trade", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // CANDLESTICK CHART CANVAS (Matching MetaTrader 4 in video)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFF0C0E14))
                .border(1.dp, CyberCardBorder)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        crosshairY = offset.y
                    }
                }
        ) {
            CandlestickChartCanvas(
                candles = candles,
                crosshairY = crosshairY,
                modifier = Modifier.fillMaxSize()
            )

            // Current price badge overlay on right
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF1A1F2C))
                    .border(1.dp, CyberCyan, RoundedCornerShape(3.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("4608.10", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // ACTIVE POSITIONS / TRADES LIST
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Open Positions (2)",
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "+$109.40",
                        color = GreenProfit,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            items(orders, key = { it.id }) { order ->
                OrderCard(order = order)
            }

            item {
                // Quick Order Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { /* Instant Sell */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RedSell),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SELL 0.10", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = { /* Instant Buy */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenProfit),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("BUY 0.10", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CandlestickChartCanvas(
    candles: List<CandleData>,
    crosshairY: Float?,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val minPrice = 4500f
        val maxPrice = 4700f
        val priceRange = maxPrice - minPrice

        // Draw horizontal gridlines
        val gridSteps = 6
        for (i in 0..gridSteps) {
            val y = height * (i.toFloat() / gridSteps)
            val price = maxPrice - (priceRange * (i.toFloat() / gridSteps))
            drawLine(
                color = Color(0x22FFFFFF),
                start = Offset(0f, y),
                end = Offset(width - 50.dp.toPx(), y),
                strokeWidth = 1f
            )
        }

        // Draw Candlesticks
        val chartWidth = width - 60.dp.toPx()
        val candleSpacing = chartWidth / candles.size
        val candleBarWidth = candleSpacing * 0.65f

        candles.forEachIndexed { index, candle ->
            val centerX = index * candleSpacing + candleSpacing / 2f

            val highY = height - ((candle.high - minPrice) / priceRange) * height
            val lowY = height - ((candle.low - minPrice) / priceRange) * height
            val openY = height - ((candle.open - minPrice) / priceRange) * height
            val closeY = height - ((candle.close - minPrice) / priceRange) * height

            val isBullish = candle.close >= candle.open
            // Matching MetaTrader colors: Purple for bullish, Black/Gray for bearish
            val candleColor = if (isBullish) Color(0xFF9D4EDD) else Color(0xFF2E3342)
            val wickColor = if (isBullish) Color(0xFFC77DFF) else Color(0xFF6B7280)

            // Wick line
            drawLine(
                color = wickColor,
                start = Offset(centerX, highY),
                end = Offset(centerX, lowY),
                strokeWidth = 1.5.dp.toPx()
            )

            // Body
            val topBody = minOf(openY, closeY)
            val bodyHeight = maxOf(Math.abs(closeY - openY), 2.dp.toPx())

            drawRect(
                color = candleColor,
                topLeft = Offset(centerX - candleBarWidth / 2f, topBody),
                size = Size(candleBarWidth, bodyHeight)
            )
        }

        // Draw active dashed entry/stop price lines
        val entryY = height - ((4613.79f - minPrice) / priceRange) * height
        drawLine(
            color = Color(0xAAFF3B30),
            start = Offset(0f, entryY),
            end = Offset(chartWidth, entryY),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )

        val tpY = height - ((4608.10f - minPrice) / priceRange) * height
        drawLine(
            color = Color(0xAA00E676),
            start = Offset(0f, tpY),
            end = Offset(chartWidth, tpY),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}

@Composable
fun OrderCard(order: TradeOrder) {
    val isProfit = order.profit >= 0
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(CyberCard)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = order.symbol,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${order.type} ${order.lots}",
                        color = if (order.type == "SELL") RedSell else GreenProfit,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "${if (isProfit) "+" else ""}$${String.format("%.2f", order.profit)}",
                    color = if (isProfit) GreenProfit else RedSell,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${order.openPrice} → ${order.currentPrice}",
                    color = TextGrayLight,
                    fontSize = 12.sp
                )
                Text(
                    text = "SL: ${order.sl}  TP: ${order.tp}",
                    color = TextGrayMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
