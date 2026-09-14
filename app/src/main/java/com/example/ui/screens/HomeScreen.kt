package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TradingBot
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.GreenProfit
import com.example.ui.theme.RedDark
import com.example.ui.theme.RedPrimary
import com.example.ui.theme.TextGrayDark
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningBackground
import com.example.ui.theme.WarningBorder
import com.example.ui.theme.WarningYellow
import com.example.ui.viewmodel.TradingViewModel

@Composable
fun HomeScreen(
    viewModel: TradingViewModel,
    modifier: Modifier = Modifier
) {
    val isCopyTradingActive by viewModel.isCopyTradingActive.collectAsState()
    val isWarningDismissed by viewModel.isWarningDismissed.collectAsState()
    val bots by viewModel.bots.collectAsState()
    val logs by viewModel.logs.collectAsState()

    val showPairsDialog by viewModel.showPairsDialog.collectAsState()
    val showLogsDialog by viewModel.showLogsDialog.collectAsState()
    val showAddBotDialog by viewModel.showAddBotDialog.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "ea_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SPACING & CYBORG LOGO
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .drawBehind {
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFF3B30).copy(alpha = if (isCopyTradingActive) pulseGlow else 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                radius = size.width * 0.8f
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, if (isCopyTradingActive) GreenProfit else RedPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_cyborg_avatar),
                            contentDescription = "Robot Mascot",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Sparky Scalper Pro V2.0",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "AUTOMATED TRADING EA",
                    color = TextGrayMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp
                )
            }

            // CONTROLS ROW: [Pairs] [ START / STOP ] [Logs]
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // PAIRS BUTTON
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .testTag("btn_pairs")
                            .clickable { viewModel.setPairsDialogVisible(true) }
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CyberSurface)
                                .border(1.dp, CyberCardBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CurrencyExchange,
                                contentDescription = "Pairs",
                                tint = TextWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pairs",
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // BIG START / STOP BUTTON (Exact centerpiece from video)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .testTag("btn_start_stop")
                            .clickable { viewModel.toggleCopyTrading() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .drawBehind {
                                    if (isCopyTradingActive) {
                                        drawCircle(
                                            color = GreenProfit.copy(alpha = pulseGlow * 0.4f),
                                            radius = size.width * 0.65f
                                        )
                                    } else {
                                        drawCircle(
                                            color = RedPrimary.copy(alpha = 0.3f),
                                            radius = size.width * 0.6f
                                        )
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(if (isCopyTradingActive) GreenProfit else RedPrimary)
                                    .border(2.dp, TextWhite.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isCopyTradingActive) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = if (isCopyTradingActive) "Stop" else "Start",
                                    tint = TextWhite,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isCopyTradingActive) "STOP" else "START",
                            color = if (isCopyTradingActive) GreenProfit else RedPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    // LOGS BUTTON
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .testTag("btn_logs")
                            .clickable { viewModel.setLogsDialogVisible(true) }
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CyberSurface)
                                .border(1.dp, CyberCardBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Logs",
                                tint = TextWhite,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Logs",
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = "Powered by EAConnect",
                    color = TextGrayMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // WARNING ALERT BANNER (Shown in video at 00:11)
            item {
                AnimatedVisibility(
                    visible = !isWarningDismissed && !isCopyTradingActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(WarningBackground)
                            .border(1.dp, WarningBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = WarningYellow,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Copy trading is not started. Press START to begin receiving trades",
                                color = TextWhite,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.dismissWarning() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = TextGrayMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ROBOT LIST SECTION
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "ROBOT LIST",
                        color = TextGrayMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            items(bots, key = { it.id }) { bot ->
                RobotCard(
                    bot = bot,
                    onToggleActive = { viewModel.toggleBot(bot.id) }
                )
            }

            // ADD NEW TRADING BOT BUTTON (From video)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CyberSurface)
                        .border(
                            1.dp,
                            CyberCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.setAddBotDialogVisible(true) }
                        .padding(vertical = 14.dp)
                        .testTag("btn_add_bot"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = RedPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add New Trading Bot",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // PAIRS DIALOG
    if (showPairsDialog) {
        PairsSelectionDialog(
            onDismiss = { viewModel.setPairsDialogVisible(false) }
        )
    }

    // LOGS DIALOG
    if (showLogsDialog) {
        LogsHistoryDialog(
            logs = logs,
            onDismiss = { viewModel.setLogsDialogVisible(false) }
        )
    }

    // ADD BOT DIALOG
    if (showAddBotDialog) {
        AddBotDialog(
            onDismiss = { viewModel.setAddBotDialogVisible(false) },
            onAdd = { name, version, pairs, tf, desc ->
                viewModel.addNewBot(name, version, pairs, tf, desc)
            }
        )
    }
}

@Composable
fun RobotCard(
    bot: TradingBot,
    onToggleActive: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CyberCard)
            .border(
                1.dp,
                if (bot.isActive) Color(0x44FF3B30) else CyberCardBorder,
                RoundedCornerShape(14.dp)
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = bot.name,
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (bot.isActive) GreenProfit else TextGrayDark)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (bot.isActive) "Active" else "Disabled",
                            color = if (bot.isActive) GreenProfit else TextGrayMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Switch(
                    checked = bot.isActive,
                    onCheckedChange = { onToggleActive() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = TextWhite,
                        checkedTrackColor = RedPrimary,
                        uncheckedThumbColor = TextGrayMuted,
                        uncheckedTrackColor = CyberSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-metrics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Pairs", color = TextGrayMuted, fontSize = 11.sp)
                    Text(bot.pairs, color = TextGrayLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Win Rate", color = TextGrayMuted, fontSize = 11.sp)
                    Text("${bot.winRate}%", color = GreenProfit, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Total Profit", color = TextGrayMuted, fontSize = 11.sp)
                    Text("+$${String.format("%.2f", bot.profitUSD)}", color = GreenProfit, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PairsSelectionDialog(onDismiss: () -> Unit) {
    val allPairs = listOf(
        "XAUUSD.mic",
        "EURUSD",
        "GBPUSD",
        "BTCUSD",
        "US30",
        "NAS100",
        "USDJPY"
    )
    var selectedPairs by remember {
        mutableStateOf(setOf("XAUUSD.mic", "EURUSD", "GBPUSD", "BTCUSD", "USDJPY"))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberSurface,
        title = {
            Text("Trading Pairs Selection", color = TextWhite, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Select forex and crypto symbols monitored by Sparky Scalper Pro:",
                    color = TextGrayMuted,
                    fontSize = 12.sp
                )
                allPairs.forEach { pair ->
                    val isChecked = selectedPairs.contains(pair)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedPairs = if (isChecked) {
                                    selectedPairs - pair
                                } else {
                                    selectedPairs + pair
                                }
                            }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(pair, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Switch(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                selectedPairs = if (checked) {
                                    selectedPairs + pair
                                } else {
                                    selectedPairs - pair
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextWhite,
                                checkedTrackColor = RedPrimary
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary)
            ) {
                Text("Save & Close", color = TextWhite)
            }
        }
    )
}

@Composable
fun LogsHistoryDialog(
    logs: List<com.example.data.model.TradeLog>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = null, tint = RedPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("EAConnect System Logs", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs) { log ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyberCard)
                            .padding(8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("[${log.timestamp}] ${log.symbol}", color = RedPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text(log.type, color = if (log.type == "TRADE") GreenProfit else CyberCyan, fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(log.message, color = TextGrayLight, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary)
            ) {
                Text("Close", color = TextWhite)
            }
        }
    )
}

@Composable
fun AddBotDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, version: String, pairs: String, tf: String, desc: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var pairs by remember { mutableStateOf("XAUUSD.mic, EURUSD") }
    var timeframe by remember { mutableStateOf("M15, H1") }
    var description by remember { mutableStateOf("Custom scalping algorithm") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CyberSurface,
        title = {
            Text("Add New Trading Bot", color = TextWhite, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Bot Name") },
                    placeholder = { Text("e.g. Cyber Trend Hunter") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = RedPrimary,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pairs,
                    onValueChange = { pairs = it },
                    label = { Text("Monitored Pairs") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = RedPrimary,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = timeframe,
                    onValueChange = { timeframe = it },
                    label = { Text("Timeframes") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = RedPrimary,
                        unfocusedBorderColor = CyberCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(name, "V1.0", pairs, timeframe, description)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                enabled = name.isNotBlank()
            ) {
                Text("Add Bot", color = TextWhite)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextGrayMuted)
            }
        }
    )
}
