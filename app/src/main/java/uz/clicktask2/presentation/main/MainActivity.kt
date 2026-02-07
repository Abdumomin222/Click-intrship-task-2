package uz.clicktask2.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import uz.clicktask2.presentation.main.ui.theme.ClickTask2Theme
import uz.clicktask2.presentation.presentation.screen.ProductScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClickTask2Theme {
                Navigator(ProductScreen())
            }
        }
    }
}