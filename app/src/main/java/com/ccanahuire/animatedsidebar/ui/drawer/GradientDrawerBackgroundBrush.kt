package com.ccanahuire.animatedsidebar.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ccanahuire.animatedsidebar.ui.theme.AnimatedSideBarTheme

@Composable
fun gradientDrawerBackgroundBrush(): Brush {
    val colors = listOf(
        Color(0xFF3C4576),
        Color(0XFF1C223A)
    )

    return with(LocalDensity.current) {
        Brush.radialGradient(
            colors = colors,
            center = Offset(200.dp.toPx(), 700.dp.toPx()),
            radius = 800.dp.toPx()
        )
    }
}

@Preview
@Composable
private fun DrawerBackgroundGradientPreview() {
    AnimatedSideBarTheme {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientDrawerBackgroundBrush())
        )
    }
}