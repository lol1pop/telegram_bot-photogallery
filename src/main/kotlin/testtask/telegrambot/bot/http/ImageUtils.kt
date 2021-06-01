package testtask.telegrambot.bot.http

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.apache.http.HttpHeaders.CONTENT_TYPE
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import java.util.concurrent.TimeUnit

fun <T> Optional<T>.getOrNull(): T? = if (isPresent) get() else null

object ImageUtils {
    private fun client() = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()
    private val httpClient by lazy { client() }

    fun validateUrl(url: String, withNewClient: Boolean = false, timeout: Long = 2000): String? =
        runCatching {
            val client = if (withNewClient) client() else httpClient
            val response =
                client.sendAsync(
                    HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build(),
                    HttpResponse.BodyHandlers.ofString()
                ).get(timeout, TimeUnit.MILLISECONDS)
            if (response.statusCode() in 200 until 300
                && response.headers().firstValue(CONTENT_TYPE).getOrNull()?.toMediaTypeOrNull()?.type == "image"
            ) response.request().uri().normalize().toString()
            else null
        }
            .getOrNull()
}
