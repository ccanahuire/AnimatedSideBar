package com.ccanahuire.animatedsidebar.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ccanahuire.animatedsidebar.R
import com.ccanahuire.animatedsidebar.ui.drawer.FancyDrawerValue
import com.ccanahuire.animatedsidebar.ui.drawer.FancyNavigationDrawer
import com.ccanahuire.animatedsidebar.ui.drawer.gradientDrawerBackgroundBrush
import com.ccanahuire.animatedsidebar.ui.drawer.rememberFancyNavigationDrawerState
import com.ccanahuire.animatedsidebar.ui.theme.AnimatedSideBarTheme

@Composable
fun HomeSideBar(
    navigationItems: List<NavigationItem>,
    modifier: Modifier = Modifier,
) {
    val contentColor = Color.White
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(modifier = modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(8.dp)
                        .size(48.dp),
                    painter = painterResource(R.drawable.android_logo),
                    contentDescription = null
                )

                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = "John Doe",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Software Developer",
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor.copy(alpha = 0.5f)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .alpha(0.2f)
            )

            navigationItems.forEach {
                SideBarItem(navigationItem = it)
            }
        }
    }
}

@Composable
fun SideBarItem(
    navigationItem: NavigationItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = navigationItem.icon, contentDescription = null)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = navigationItem.title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun HomeSideBarPreview() {
    AnimatedSideBarTheme {
        val drawerState = rememberFancyNavigationDrawerState(initialValue = FancyDrawerValue.Open)
        FancyNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                HomeSideBar(
                    navigationItems = listOf(
                        NavigationItem(
                            title = "Home",
                            icon = Icons.Rounded.Home
                        )
                    ),
                    modifier = Modifier.windowInsetsPadding(DrawerDefaults.windowInsets),
                )
            },
            drawerBackgroundBrush = gradientDrawerBackgroundBrush(),
            content = { Box(modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
            ) }
        )
    }
}