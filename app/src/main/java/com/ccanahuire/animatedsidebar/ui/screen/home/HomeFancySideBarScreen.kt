package com.ccanahuire.animatedsidebar.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ccanahuire.animatedsidebar.ui.drawer.FancyDrawerValue
import com.ccanahuire.animatedsidebar.ui.drawer.FancyNavigationDrawer
import com.ccanahuire.animatedsidebar.ui.drawer.gradientDrawerBackgroundBrush
import com.ccanahuire.animatedsidebar.ui.drawer.rememberFancyNavigationDrawerState
import com.ccanahuire.animatedsidebar.ui.theme.AnimatedSideBarTheme
import kotlinx.coroutines.launch

private val homeNavigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = Icons.Filled.Home
    ),
    NavigationItem(
        title = "Profile",
        icon = Icons.Filled.Person
    ),
    NavigationItem(
        title = "Settings",
        icon = Icons.Filled.Settings
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeFancySideBarScreen(
    modifier: Modifier = Modifier
) {
    val drawerState = rememberFancyNavigationDrawerState(initialValue = FancyDrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    FancyNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            HomeSideBar(
                navigationItems = homeNavigationItems,
                modifier = Modifier.windowInsetsPadding(DrawerDefaults.windowInsets),
            )
        },
        drawerBackgroundBrush = gradientDrawerBackgroundBrush()
    ) {
        HomeScreenContent(
            onClickNavigationIcon = {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onClickNavigationIcon: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Home".uppercase(),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClickNavigationIcon) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Home content screen."
            )
        }
    }
}

@Preview
@Composable
private fun HomeMaterialSideBarScreenPreview() {
    AnimatedSideBarTheme {
        HomeFancySideBarScreen()
    }
}