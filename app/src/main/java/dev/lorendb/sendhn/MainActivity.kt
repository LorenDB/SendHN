package dev.lorendb.sendhn

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.lorendb.sendhn.ui.theme.SendHNTheme
import kotlinx.coroutines.*
import android.os.Bundle
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SendHNTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var url by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Enter URL") }
        )
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        SendToHN(context).send(url)
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Failed to send to HN", e)
                    }
                }
            }
        ) {
            Text("Share to HN")
        }
    }
}