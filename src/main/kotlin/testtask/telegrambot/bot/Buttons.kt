package testtask.telegrambot.bot

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import testtask.telegrambot.bot.constant.Texts
import testtask.telegrambot.bot.database.model.UserPhoto

val notPhotoButtons by lazy { listOf("addNewPhotos", "photoList", "photoUpdateAuthor", "photoDelete", "back", "yes", "no") }

val mainMenuButtons by lazy {
    InlineKeyboardMarkup.create(
        listOf(InlineKeyboardButton.CallbackData(text = Texts.add_new_photos_button, callbackData = "addNewPhotos")),
        listOf(InlineKeyboardButton.CallbackData(text = Texts.photo_list_button, callbackData = "photoList"))
    )
}

val photoMenuButton by lazy {
    InlineKeyboardMarkup.create(
        listOf(InlineKeyboardButton.CallbackData(text = Texts.photo_update_author_button, callbackData = "photoUpdateAuthor")),
        listOf(InlineKeyboardButton.CallbackData(text = Texts.photo_delete_button, callbackData = "photoDelete")),
        listOf(InlineKeyboardButton.CallbackData(text = Texts.back_button, callbackData = "back"))
    )
}

val backButton by lazy {
    InlineKeyboardMarkup.create(
        listOf(InlineKeyboardButton.CallbackData(text = Texts.back_button, callbackData = "back"))
    )
}

val confirmationButton by lazy {
    InlineKeyboardMarkup.createSingleRowKeyboard(
        InlineKeyboardButton.CallbackData(text = Texts.yes_button, callbackData = "yes"),
        InlineKeyboardButton.CallbackData(text = Texts.no_button, callbackData = "no"),
    )
}

fun generatePhotoButtons(photoList: List<UserPhoto>): InlineKeyboardMarkup = InlineKeyboardMarkup.create(
    photoList.mapNotNull { up ->
        up.photo?.let {
            listOf(InlineKeyboardButton.CallbackData(Texts.photoListButtonTemplate(it), up.userId!!))
        }
    }
)
