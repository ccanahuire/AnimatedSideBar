package com.ccanahuire.animatedsidebar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ccanahuire.animatedsidebar.ui.screen.home.HomeFancySideBarScreen
import com.ccanahuire.animatedsidebar.ui.theme.AnimatedSideBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimatedSideBarTheme {
                HomeFancySideBarScreen()
            }
        }
    }
}
