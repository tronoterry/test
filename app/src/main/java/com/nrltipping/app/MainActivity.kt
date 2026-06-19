package com.nrltipping.app

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.nrltipping.app.ui.navigation.NrlNavGraph
import com.nrltipping.app.ui.theme.NrlTippingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NrlTippingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NrlNavGraph()
                }
            }
        }
    }
}
