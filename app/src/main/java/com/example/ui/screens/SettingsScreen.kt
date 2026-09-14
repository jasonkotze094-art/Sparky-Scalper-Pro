package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.GreenProfit
import com.example.ui.theme.RedPrimary
import com.example.ui.theme.TextGrayLight
import com.example.ui.theme.TextGrayMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.TradingViewModel

@Composable
fun SettingsScreen(
    viewModel: TradingViewModel,
    modifier: Modifier = Modifier
) {
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val hapticsEnabled by viewModel.hapticsEnabled.collectAsState()
    val lotSize by viewModel.lotSize.collectAsState()
    val riskPercent by viewModel.riskPercent.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // TOP HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Settings",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // USER ACCOUNT CARD (Exact info from video: Diego Moshe VaultMarkets-Live DC 2)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberCard)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF22283A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "User Avatar",
                                    tint = TextWhite,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Diego Moshe",
                                    color = TextWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "VaultMarkets (Pty) Ltd",
                                    color = TextGrayLight,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "5555737 - VaultMarkets-Live DC 2",
                                    color = GreenProfit,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Open",
                            tint = TextGrayMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // EA TRADING PARAMETERS
            item {
                Text(
                    text = "EA EXECUTION PARAMETERS",
                    color = TextGrayMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberCard)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column {
                        SettingValueRow(
                            label = "Default Lot Size",
                            value = lotSize,
                            options = listOf("0.01", "0.05", "0.10", "0.25"),
                            onSelect = { viewModel.setLotSize(it) }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        SettingValueRow(
                            label = "Risk Per Trade",
                            value = riskPercent,
                            options = listOf("1.0%", "1.5%", "2.0%", "3.0%"),
                            onSelect = { viewModel.setRiskPercent(it) }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        SettingToggleRow(
                            icon = Icons.Default.Notifications,
                            label = "Trade Push Notifications",
                            checked = notificationsEnabled,
                            onToggle = { viewModel.toggleNotifications() }
                        )

                        SettingToggleRow(
                            icon = Icons.Default.Vibration,
                            label = "Haptic Trade Confirmations",
                            checked = hapticsEnabled,
                            onToggle = { viewModel.toggleHaptics() }
                        )
                    }
                }
            }

            // METATRADER INTEGRATION LINKS (From MT4 screen in video)
            item {
                Text(
                    text = "TERMINAL SERVICES",
                    color = TextGrayMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberCard)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Column {
                        SettingsItemRow(
                            icon = Icons.Default.Mail,
                            title = "Mailbox",
                            subtitle = "Built-in Virtual Hosting - trading robots active"
                        )
                        SettingsItemRow(
                            icon = Icons.Default.Newspaper,
                            title = "Tradays",
                            subtitle = "Economic calendar & high impact events"
                        )
                        SettingsItemRow(
                            icon = Icons.Default.SmartToy,
                            title = "MQL5 Algo Trading",
                            subtitle = "Cloud VPS synchronization: Connected"
                        )
                        SettingsItemRow(
                            icon = Icons.Default.Password,
                            title = "OTP One-Time Password",
                            subtitle = "Generator enabled"
                        )
                        SettingsItemRow(
                            icon = Icons.Default.Language,
                            title = "Interface",
                            subtitle = "English"
                        )
                    }
                }
            }

            // ABOUT SPARKYS SCALPER PRO
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberSurface)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sparky Scalper Pro V2.0",
                            color = RedPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "EAConnect Engine Build 2026.4",
                            color = TextGrayMuted,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Automated high-frequency chart scanner and copy trading assistant.",
                            color = TextGrayLight,
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun SettingValueRow(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(value, color = CyberCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { opt ->
                val isSelected = opt == value
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) RedPrimary else CyberSurface)
                        .clickable { onSelect(opt) }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = opt,
                        color = if (isSelected) TextWhite else TextGrayMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun SettingToggleRow(
    icon: ImageVector,
    label: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = TextGrayLight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(label, color = TextWhite, fontSize = 13.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextWhite,
                checkedTrackColor = RedPrimary,
                uncheckedThumbColor = TextGrayMuted,
                uncheckedTrackColor = CyberSurface
            )
        )
    }
}

@Composable
fun SettingsItemRow(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = TextGrayLight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, color = TextGrayMuted, fontSize = 11.sp)
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Next",
            tint = TextGrayMuted,
            modifier = Modifier.size(13.dp)
        )
    }
}
