package testtask.telegrambot.bot.http

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import testtask.telegrambot.bot.database.model.Photo
import java.io.IOException

object HttpClient {
    private val httpClient by lazy { OkHttpClient() }

    fun getData(url: String): List<Photo> {
        val request = Request.Builder()
            .url(url)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            response.body?.let {
                return Json.decodeFromString<List<Photo>>(it.string())
            }
        }
        return emptyList()
    }
}
