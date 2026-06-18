package com.crf450rl.companion.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.BatteryAlert
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Carpenter
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.OilBarrel
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.ui.graphics.vector.ImageVector

/** Schematic diagrams available to illustrate a guide step. Rendered as original line-art, not manual scans. */
enum class DiagramType {
    ENGINE_OVERVIEW,
    OIL_LEVEL,
    CHAIN_SLACK,
    VALVE_CLEARANCE,
    SPARK_PLUG_GAP,
    SUSPENSION_SAG,
    TIRE_PRESSURE,
    BRAKE_PAD_WEAR,
    AIR_FILTER,
    COOLANT_LEVEL,
    BATTERY_FUSE,
    TORQUE_WRENCH
}

data class GuideStep(
    val title: String,
    val description: String,
    val diagram: DiagramType? = null,
    val tip: String? = null,
    val warning: String? = null
)

enum class Difficulty(val label: String) {
    EASY("Easy"),
    MODERATE("Moderate"),
    ADVANCED("Advanced")
}

data class Guide(
    val id: String,
    val title: String,
    val category: String,
    val icon: ImageVector,
    val difficulty: Difficulty,
    val timeEstimate: String,
    val interval: String? = null,
    val toolsNeeded: List<String> = emptyList(),
    val partsNeeded: List<String> = emptyList(),
    val summary: String,
    val steps: List<GuideStep>,
    val safetyNote: String? = null
)

data class TroubleshootingIssue(
    val id: String,
    val title: String,
    val category: String,
    val icon: ImageVector,
    val symptoms: List<String>,
    val summary: String,
    val steps: List<GuideStep>
)

data class ModGuide(
    val id: String,
    val title: String,
    val category: String,
    val icon: ImageVector,
    val difficulty: Difficulty,
    val timeEstimate: String,
    val toolsNeeded: List<String> = emptyList(),
    val partsNeeded: List<String> = emptyList(),
    val summary: String,
    val legalNote: String? = null,
    val steps: List<GuideStep>
)

data class SpecRow(val label: String, val value: String)
data class SpecSection(val title: String, val rows: List<SpecRow>)

object Icons2 {
    val Maintenance: ImageVector = Icons.Filled.Build
    val Troubleshooting: ImageVector = Icons.Outlined.Warning
    val Mods: ImageVector = Icons.Filled.Construction
    val Specs: ImageVector = Icons.Outlined.Settings
    val Bike: ImageVector = Icons.Outlined.DirectionsBike
    val Oil: ImageVector = Icons.Outlined.OilBarrel
    val Air: ImageVector = Icons.Outlined.Air
    val Chain: ImageVector = Icons.Filled.Handyman
    val Valve: ImageVector = Icons.Outlined.Tune
    val Plug: ImageVector = Icons.Outlined.Bolt
    val Coolant: ImageVector = Icons.Outlined.Opacity
    val Brake: ImageVector = Icons.Outlined.Compress
    val Suspension: ImageVector = Icons.Outlined.Vibration
    val Tire: ImageVector = Icons.Filled.Speed
    val Battery: ImageVector = Icons.Outlined.BatteryAlert
    val Electrical: ImageVector = Icons.Outlined.PowerSettingsNew
    val Fuel: ImageVector = Icons.Outlined.LocalGasStation
    val Exhaust: ImageVector = Icons.Outlined.Brightness5
    val Wrench: ImageVector = Icons.Outlined.Build
    val Fabrication: ImageVector = Icons.Outlined.Carpenter
}
