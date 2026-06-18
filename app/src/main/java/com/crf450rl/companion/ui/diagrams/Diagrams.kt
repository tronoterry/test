package com.crf450rl.companion.ui.diagrams

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crf450rl.companion.data.DiagramType
import kotlin.math.cos
import kotlin.math.sin

private val InkColor = Color(0xFF1C1C1C)
private val AccentColor = Color(0xFFE60012)
private val MutedColor = Color(0xFF6B6B6B)

@Composable
fun DiagramView(type: DiagramType, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        val pad = 16.dp.toPx()
        drawDiagram(type, textMeasurer, pad)
    }
}

private fun label(scope: DrawScope, tm: TextMeasurer, text: String, pos: Offset, color: Color = InkColor, size: Float = 12f) {
    scope.drawText(
        textMeasurer = tm,
        text = text,
        topLeft = pos,
        style = TextStyle(color = color, fontSize = size.sp)
    )
}

private fun DrawScope.drawDiagram(type: DiagramType, tm: TextMeasurer, pad: Float) {
    when (type) {
        DiagramType.ENGINE_OVERVIEW -> drawEngineOverview(tm)
        DiagramType.OIL_LEVEL -> drawOilLevel(tm)
        DiagramType.CHAIN_SLACK -> drawChainSlack(tm)
        DiagramType.VALVE_CLEARANCE -> drawValveClearance(tm)
        DiagramType.SPARK_PLUG_GAP -> drawSparkPlugGap(tm)
        DiagramType.SUSPENSION_SAG -> drawSuspensionSag(tm)
        DiagramType.TIRE_PRESSURE -> drawTirePressure(tm)
        DiagramType.BRAKE_PAD_WEAR -> drawBrakePadWear(tm)
        DiagramType.AIR_FILTER -> drawAirFilter(tm)
        DiagramType.COOLANT_LEVEL -> drawCoolantLevel(tm)
        DiagramType.BATTERY_FUSE -> drawBatteryFuse(tm)
        DiagramType.TORQUE_WRENCH -> drawTorqueWrench(tm)
    }
}

private fun DrawScope.drawEngineOverview(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    // Simplified side-on bike silhouette: two wheels + frame triangle + engine block.
    val groundY = h * 0.82f
    drawLine(MutedColor, Offset(0f, groundY), Offset(w, groundY), strokeWidth = 2f)
    val rearWheel = Offset(w * 0.25f, groundY)
    val frontWheel = Offset(w * 0.78f, groundY)
    val wheelR = h * 0.16f
    drawCircle(InkColor, wheelR, rearWheel, style = Stroke(width = 4f))
    drawCircle(InkColor, wheelR, frontWheel, style = Stroke(width = 4f))
    val engineCenter = Offset(w * 0.48f, groundY - h * 0.28f)
    drawRoundRect(
        color = AccentColor,
        topLeft = Offset(engineCenter.x - w * 0.12f, engineCenter.y - h * 0.12f),
        size = Size(w * 0.24f, h * 0.24f)
    )
    // frame lines
    drawLine(InkColor, rearWheel, engineCenter, strokeWidth = 4f)
    drawLine(InkColor, engineCenter, frontWheel, strokeWidth = 4f)
    drawLine(InkColor, engineCenter, Offset(w * 0.5f, groundY - h * 0.55f), strokeWidth = 4f)
    drawLine(InkColor, Offset(w * 0.5f, groundY - h * 0.55f), frontWheel, strokeWidth = 4f)
    label(this, tm, "Drain bolt", Offset(engineCenter.x - w * 0.10f, engineCenter.y + h * 0.16f))
    label(this, tm, "Chain", Offset((rearWheel.x + engineCenter.x) / 2 - 10f, groundY - h * 0.05f))
    label(this, tm, "Air filter", Offset(w * 0.42f, groundY - h * 0.62f))
}

private fun DrawScope.drawOilLevel(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val boxLeft = w * 0.3f
    val boxTop = h * 0.15f
    val boxW = w * 0.4f
    val boxH = h * 0.6f
    drawRect(InkColor, Offset(boxLeft, boxTop), Size(boxW, boxH), style = Stroke(width = 3f))
    val oilTop = boxTop + boxH * 0.35f
    drawRect(AccentColor.copy(alpha = 0.55f), Offset(boxLeft, oilTop), Size(boxW, boxH - boxH * 0.35f))
    val upperLine = boxTop + boxH * 0.3f
    val lowerLine = boxTop + boxH * 0.7f
    drawLine(InkColor, Offset(boxLeft - 14f, upperLine), Offset(boxLeft + boxW + 14f, upperLine), strokeWidth = 2f, cap = StrokeCap.Round)
    drawLine(InkColor, Offset(boxLeft - 14f, lowerLine), Offset(boxLeft + boxW + 14f, lowerLine), strokeWidth = 2f, cap = StrokeCap.Round)
    label(this, tm, "Upper level", Offset(boxLeft + boxW + 18f, upperLine - 14f))
    label(this, tm, "Lower level", Offset(boxLeft + boxW + 18f, lowerLine - 6f))
    label(this, tm, "Sight window", Offset(boxLeft, boxTop - 24f), color = MutedColor)
}

private fun DrawScope.drawChainSlack(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val midY = h * 0.55f
    val sproc1 = Offset(w * 0.2f, midY)
    val sproc2 = Offset(w * 0.8f, midY)
    drawCircle(InkColor, h * 0.14f, sproc1, style = Stroke(width = 4f))
    drawCircle(InkColor, h * 0.22f, sproc2, style = Stroke(width = 4f))
    // chain run, sagging in the middle to show slack
    val midX = (sproc1.x + sproc2.x) / 2
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(sproc1.x, midY + h * 0.1f)
        quadraticBezierTo(midX, midY + h * 0.22f, sproc2.x, midY + h * 0.16f)
    }
    drawPath(path, AccentColor, style = Stroke(width = 5f))
    drawLine(MutedColor, Offset(midX, midY + h * 0.1f), Offset(midX, midY + h * 0.22f), strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f)))
    label(this, tm, "Up/down play", Offset(midX - 50f, midY + h * 0.27f))
    label(this, tm, "Measure at midpoint", Offset(w * 0.28f, midY - h * 0.32f), color = MutedColor)
}

private fun DrawScope.drawValveClearance(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val camCenter = Offset(w * 0.5f, h * 0.35f)
    drawCircle(InkColor, h * 0.14f, camCenter, style = Stroke(width = 4f))
    // cam lobe bump
    val lobePath = androidx.compose.ui.graphics.Path().apply {
        moveTo(camCenter.x - 10f, camCenter.y + h * 0.1f)
        lineTo(camCenter.x, camCenter.y + h * 0.22f)
        lineTo(camCenter.x + 10f, camCenter.y + h * 0.1f)
    }
    drawPath(lobePath, AccentColor, style = Stroke(width = 4f))
    val valveTop = camCenter.y + h * 0.22f
    drawRect(InkColor, Offset(camCenter.x - 4f, valveTop), Size(8f, h * 0.32f))
    val gaugeY = valveTop - 4f
    drawLine(AccentColor, Offset(camCenter.x - 30f, gaugeY), Offset(camCenter.x + 30f, gaugeY), strokeWidth = 3f)
    label(this, tm, "Cam lobe", Offset(camCenter.x + h * 0.16f, camCenter.y - 8f))
    label(this, tm, "Feeler gauge", Offset(camCenter.x + 36f, gaugeY - 4f))
    label(this, tm, "Valve stem", Offset(camCenter.x + 14f, valveTop + h * 0.12f))
}

private fun DrawScope.drawSparkPlugGap(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val topY = h * 0.2f
    drawRect(MutedColor, Offset(w * 0.42f, topY), Size(w * 0.16f, h * 0.18f))
    drawLine(InkColor, Offset(w * 0.5f, topY + h * 0.18f), Offset(w * 0.5f, h * 0.62f), strokeWidth = 6f)
    val electrodeY = h * 0.62f
    drawLine(InkColor, Offset(w * 0.5f, electrodeY), Offset(w * 0.5f, electrodeY + h * 0.1f), strokeWidth = 6f)
    val gapTop = electrodeY + h * 0.1f
    val gapBottom = gapTop + h * 0.06f
    drawLine(AccentColor, Offset(w * 0.5f, gapTop), Offset(w * 0.5f, gapBottom), strokeWidth = 3f)
    drawLine(InkColor, Offset(w * 0.38f, gapBottom), Offset(w * 0.62f, gapBottom), strokeWidth = 6f)
    label(this, tm, "Gap", Offset(w * 0.56f, (gapTop + gapBottom) / 2 - 6f), color = AccentColor)
    label(this, tm, "Ground electrode", Offset(w * 0.62f, gapBottom - 4f))
    label(this, tm, "Center electrode", Offset(w * 0.08f, electrodeY))
}

private fun DrawScope.drawSuspensionSag(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val axleX = w * 0.3f
    val refX = w * 0.3f
    val refTopY = h * 0.12f
    val axleUnloadedY = h * 0.78f
    val axleLoadedY = h * 0.62f
    drawLine(InkColor, Offset(refX, refTopY), Offset(refX, h * 0.9f), strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 6f)))
    drawCircle(MutedColor, h * 0.06f, Offset(axleX, axleUnloadedY), style = Stroke(width = 3f))
    drawCircle(AccentColor, h * 0.06f, Offset(axleX, axleLoadedY), style = Stroke(width = 3f))
    drawLine(MutedColor, Offset(refX - 12f, refTopY), Offset(refX + 12f, refTopY), strokeWidth = 2f)
    drawLine(MutedColor, Offset(axleX - 30f, axleUnloadedY), Offset(axleX + 30f, axleUnloadedY), strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 6f)))
    drawLine(AccentColor, Offset(axleX - 30f, axleLoadedY), Offset(axleX + 30f, axleLoadedY), strokeWidth = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 6f)))
    label(this, tm, "Reference point", Offset(refX + 16f, refTopY - 18f))
    label(this, tm, "L1: unloaded", Offset(axleX + 36f, axleUnloadedY - 8f), color = MutedColor)
    label(this, tm, "L2: loaded (race sag)", Offset(axleX + 36f, axleLoadedY - 8f), color = AccentColor)
}

private fun DrawScope.drawTirePressure(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val center = Offset(w * 0.38f, h * 0.5f)
    val r = h * 0.32f
    drawCircle(InkColor, r, center, style = Stroke(width = 8f))
    drawCircle(MutedColor, r * 0.55f, center, style = Stroke(width = 3f))
    // gauge
    val gaugeCenter = Offset(w * 0.78f, h * 0.45f)
    val gaugeR = h * 0.22f
    drawCircle(InkColor, gaugeR, gaugeCenter, style = Stroke(width = 3f))
    val needleAngle = Math.toRadians(-50.0)
    drawLine(
        AccentColor,
        gaugeCenter,
        Offset(gaugeCenter.x + (gaugeR * 0.8f * cos(needleAngle)).toFloat(), gaugeCenter.y + (gaugeR * 0.8f * sin(needleAngle)).toFloat()),
        strokeWidth = 3f
    )
    label(this, tm, "Tire", Offset(center.x - 14f, center.y + r + 14f))
    label(this, tm, "PSI gauge", Offset(gaugeCenter.x - 28f, gaugeCenter.y + gaugeR + 14f))
}

private fun DrawScope.drawBrakePadWear(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val discCenter = Offset(w * 0.5f, h * 0.5f)
    val discR = h * 0.34f
    drawCircle(MutedColor, discR, discCenter, style = Stroke(width = 6f))
    drawCircle(MutedColor, discR * 0.3f, discCenter, style = Stroke(width = 3f))
    // caliper body straddling the disc edge
    val capLeft = discCenter.x + discR - 10f
    drawRoundRect(InkColor, Offset(capLeft, discCenter.y - h * 0.18f), Size(w * 0.16f, h * 0.36f))
    // pads
    drawRect(AccentColor, Offset(capLeft + 6f, discCenter.y - h * 0.14f), Size(10f, h * 0.28f))
    drawRect(AccentColor, Offset(capLeft + w * 0.1f, discCenter.y - h * 0.14f), Size(10f, h * 0.28f))
    label(this, tm, "Disc", Offset(discCenter.x - 16f, discCenter.y - discR - 18f))
    label(this, tm, "Pads", Offset(capLeft + 2f, discCenter.y + h * 0.18f))
    label(this, tm, "Caliper", Offset(capLeft, discCenter.y - h * 0.22f), color = MutedColor)
}

private fun DrawScope.drawAirFilter(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val cx = w * 0.5f
    val cy = h * 0.55f
    val rOuter = h * 0.3f
    val rInner = h * 0.14f
    drawCircle(AccentColor.copy(alpha = 0.5f), rOuter, Offset(cx, cy), style = Stroke(width = rOuter - rInner))
    drawCircle(InkColor, rInner, Offset(cx, cy), style = Stroke(width = 3f))
    repeat(10) { i ->
        val angle = Math.toRadians((i * 36).toDouble())
        val inner = Offset(cx + (rInner * cos(angle)).toFloat(), cy + (rInner * sin(angle)).toFloat())
        val outer = Offset(cx + (rOuter * cos(angle)).toFloat(), cy + (rOuter * sin(angle)).toFloat())
        drawLine(InkColor.copy(alpha = 0.3f), inner, outer, strokeWidth = 1.5f)
    }
    label(this, tm, "Foam element", Offset(cx - rOuter, cy + rOuter + 16f))
    label(this, tm, "Cage", Offset(cx - 22f, cy + 4f), color = MutedColor)
}

private fun DrawScope.drawCoolantLevel(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val boxLeft = w * 0.32f
    val boxTop = h * 0.15f
    val boxW = w * 0.36f
    val boxH = h * 0.55f
    drawRoundRect(InkColor, Offset(boxLeft, boxTop), Size(boxW, boxH), style = Stroke(width = 3f))
    val fillTop = boxTop + boxH * 0.4f
    drawRect(Color(0xFF2E8B57).copy(alpha = 0.6f), Offset(boxLeft, fillTop), Size(boxW, boxH - boxH * 0.4f))
    val fullLine = boxTop + boxH * 0.35f
    val lowLine = boxTop + boxH * 0.75f
    drawLine(InkColor, Offset(boxLeft - 14f, fullLine), Offset(boxLeft + boxW + 14f, fullLine), strokeWidth = 2f)
    drawLine(InkColor, Offset(boxLeft - 14f, lowLine), Offset(boxLeft + boxW + 14f, lowLine), strokeWidth = 2f)
    label(this, tm, "FULL", Offset(boxLeft + boxW + 18f, fullLine - 12f))
    label(this, tm, "LOW", Offset(boxLeft + boxW + 18f, lowLine - 6f))
    label(this, tm, "Reservoir", Offset(boxLeft, boxTop - 22f), color = MutedColor)
}

private fun DrawScope.drawBatteryFuse(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val battLeft = w * 0.12f
    val battTop = h * 0.25f
    drawRoundRect(InkColor, Offset(battLeft, battTop), Size(w * 0.3f, h * 0.4f), style = Stroke(width = 4f))
    drawLine(AccentColor, Offset(battLeft + w * 0.08f, battTop), Offset(battLeft + w * 0.08f, battTop - 12f), strokeWidth = 4f)
    drawLine(MutedColor, Offset(battLeft + w * 0.22f, battTop), Offset(battLeft + w * 0.22f, battTop - 12f), strokeWidth = 4f)
    label(this, tm, "+", Offset(battLeft + w * 0.06f, battTop - 28f), color = AccentColor, size = 16f)
    label(this, tm, "-", Offset(battLeft + w * 0.20f, battTop - 28f), color = MutedColor, size = 16f)
    label(this, tm, "Battery", Offset(battLeft, battTop + h * 0.44f))

    val fuseLeft = w * 0.58f
    val fuseTop = h * 0.3f
    repeat(3) { i ->
        val y = fuseTop + i * h * 0.16f
        drawRoundRect(MutedColor, Offset(fuseLeft, y), Size(w * 0.26f, h * 0.1f), style = Stroke(width = 2f))
    }
    drawRoundRect(AccentColor, Offset(fuseLeft, fuseTop + h * 0.16f), Size(w * 0.26f, h * 0.1f), style = Stroke(width = 3f))
    label(this, tm, "Fuse box", Offset(fuseLeft, fuseTop - 18f))
}

private fun DrawScope.drawTorqueWrench(tm: TextMeasurer) {
    val w = size.width
    val h = size.height
    val pivot = Offset(w * 0.32f, h * 0.55f)
    drawLine(InkColor, pivot, Offset(w * 0.85f, h * 0.3f), strokeWidth = 6f, cap = StrokeCap.Round)
    drawCircle(InkColor, h * 0.13f, pivot, style = Stroke(width = 5f))
    drawCircle(MutedColor, h * 0.06f, pivot)
    // direction arc + arrow showing rotation
    drawArc(
        color = AccentColor,
        startAngle = -40f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(pivot.x - h * 0.22f, pivot.y - h * 0.22f),
        size = Size(h * 0.44f, h * 0.44f),
        style = Stroke(width = 3f)
    )
    label(this, tm, "Torque value", Offset(w * 0.6f, h * 0.22f), color = MutedColor)
    label(this, tm, "Bolt", Offset(pivot.x - 14f, pivot.y + h * 0.2f))
}
