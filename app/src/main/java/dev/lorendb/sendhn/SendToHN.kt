package dev.lorendb.sendhn

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class SendToHN(private val context: Context) {
    private val client = OkHttpClient()

    suspend fun send(url: String) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw InvalidUrlException("URL must start with http:// or https://")
        }

        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).execute().use { response ->
            var title = ""
            if (response.isSuccessful) {
                val document: Document = Jsoup.parse(response.body?.string().toString())
                val titles = document.select("title")
                if (titles.isNotEmpty())
                    title = titles[0].text()
            } else {
                throw FailedToLoadWebpageTitleException("Failed to load webpage")
            }
            val hnUrl = "https://news.ycombinator.com/submitlink?u=$url&t=$title"
            withContext(Dispatchers.Main) {
                val hnIntent = Intent(Intent.ACTION_VIEW, Uri.parse(hnUrl))
                context.startActivity(hnIntent)
            }
        }
    }

    companion object {
        class InvalidUrlException(message: String) : Exception(message)
        class FailedToLoadWebpageTitleException(message: String) : Exception(message)
    }
}