package dev.lorendb.sendhn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import android.widget.Toast

class ShareHandlerActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        val url = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (url != null) {
            val self = this
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    SendToHN(self).send(url)
                } catch (e: Exception) {
                    Log.e("ShareHandlerActivity", "Failed to send to HN", e)
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(self, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        finishAndRemoveTask()
    }
}
