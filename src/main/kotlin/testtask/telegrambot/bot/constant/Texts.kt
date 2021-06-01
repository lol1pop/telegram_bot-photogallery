package testtask.telegrambot.bot.constant

import testtask.telegrambot.bot.database.model.Photo

object Texts {
    val add_new_photos_message = """
    Send me a specific format link to a list of photos from picsum.photos like this:
    https://picsum.photos/v2/list?page=2&limit=100
    """.trimIndent()
    val onboarding_message = """
    Hi, this is onboarding...
    $add_new_photos_message
    """.trimIndent()
    const val photos_saved = "Photos saved"

    const val main_manu = "Main menu"
    const val add_new_photos_button = "➕ Add new photos"
    const val photo_list_button = "📃 Photo list"

    const val photo_list = "Photo List"
    fun photoListButtonTemplate(photo: Photo): String = "${photo.author} (${photo.id})"
    fun photoDescriptionTemplate(photo: Photo) = """
    Author: ${photo.author}
    ID: ${photo.id}
    Size: ${photo.width} x ${photo.height}
    URL: ${photo.url}
    Download URL: ${photo.downloadUrl}
    """.trimIndent()
    const val photo_update_author_button = "✏️ Update Author"
    const val photo_delete_button = "🗑 Delete"

    const val photo_enter_author_name = "Enter a new author name"
    const val photo_autor_name_updated = "The author successfully updated"

    val photo_delete_confirmation_template = "Are you sure you want to delete this photo?".trimIndent()

    const val yes_button = "✅ Yes"
    const val no_button = "❎ No"

    const val back_button = "🔙 Back"
}
