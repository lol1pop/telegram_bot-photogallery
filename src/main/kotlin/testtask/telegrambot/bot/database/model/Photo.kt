package testtask.telegrambot.bot.database.model

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonNames

@Serializable
data class UserPhoto(
    val id: Long? = null,
    val userId: String? = null,
    val photo: Photo? = null
)

@Serializable
data class Photo(
    val id: String? = null,
    val author: String? = null,
    val width: Long? = null,
    val height: Long? = null,
    val url: String? = null,

    @JsonNames("download_url")
    val downloadUrl: String? = null,
)
