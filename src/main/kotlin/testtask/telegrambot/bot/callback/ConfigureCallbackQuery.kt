package testtask.telegrambot.bot.callback

import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import testtask.telegrambot.bot.*
import testtask.telegrambot.bot.constant.Texts
import testtask.telegrambot.bot.database.Firestore

object ConfigureCallbackQuery {

    private fun callbackQueryBack(dispatcher: Dispatcher) = dispatcher.callbackQuery("back") {
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        Waiting.waitingPhoto = ""
        Waiting.waitingDelete = false
        Waiting.waitingUpdate = false
        Waiting.waitingEnterText = false
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = Texts.main_manu,
            replyMarkup = mainMenuButtons
        )
    }
    private fun callbackQueryAddNewPhotos(dispatcher: Dispatcher) = dispatcher.callbackQuery("addNewPhotos") {
        Waiting.waitingEnterText = true
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        bot.sendMessage(
            ChatId.fromId(chatId),
            Texts.add_new_photos_message,
            replyMarkup = backButton
        )
    }
    private fun callbackQueryPhotoList(dispatcher: Dispatcher) = dispatcher.callbackQuery("photoList") {
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        val photoList = Firestore.findById(chatId)
        val buttons = generatePhotoButtons(photoList)
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = Texts.photo_list,
            replyMarkup = buttons
        )
    }
    private fun callbackQueryPhotoUpdateAuthor(dispatcher: Dispatcher) = dispatcher.callbackQuery("photoUpdateAuthor") {
        if(Waiting.waitingPhoto.isBlank()) return@callbackQuery
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        bot.sendMessage(
            ChatId.fromId(chatId),
            Texts.photo_enter_author_name
        )
        Waiting.waitingUpdate = true
    }
    private fun callbackQueryPhotoDelete(dispatcher: Dispatcher) = dispatcher.callbackQuery("photoDelete") {
        if(Waiting.waitingPhoto.isBlank()) return@callbackQuery
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        Waiting.waitingDelete = true
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = Texts.photo_delete_confirmation_template,
            replyMarkup = confirmationButton
        )
    }
    private fun callbackQueryYes(dispatcher: Dispatcher) = dispatcher.callbackQuery("yes") {
        if(Waiting.waitingPhoto.isBlank() && !Waiting.waitingDelete) return@callbackQuery
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        Firestore.delete(Waiting.waitingPhoto)
        Waiting.waitingPhoto = ""
        Waiting.waitingDelete = false
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = Texts.main_manu,
            replyMarkup = mainMenuButtons
        )
    }
    private fun callbackQueryNo(dispatcher: Dispatcher) = dispatcher.callbackQuery("no") {
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        Waiting.waitingDelete = false
        bot.sendMessage(
            chatId = ChatId.fromId(chatId),
            text = Texts.main_manu,
            replyMarkup = mainMenuButtons
        )
    }
    private fun callbackQuery(dispatcher: Dispatcher) = dispatcher.callbackQuery {
        if(Waiting.waitingDelete || Waiting.waitingUpdate) return@callbackQuery
        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
        if (notPhotoButtons.contains(callbackQuery.data)) return@callbackQuery
        val photo = Firestore.findByUserIdAndPhotoId(callbackQuery.data)?.photo
        if(photo == null) {
            bot.sendMessage(ChatId.fromId(chatId), "none photo")
            return@callbackQuery
        }
//                val imageUrl = ImageUtils.validateUrl(photo.downloadUrl!!)?.let {
//                    bot.sendMediaGroup(
//                        chatId = ChatId.fromId(chatId),
//                        mediaGroup = MediaGroup.from(
//                            InputMediaPhoto(
//                                media = TelegramFile.ByUrl(it),
//                                caption = photo.author!!
//                            )
//                        ))
//                }
        Waiting.waitingPhoto = callbackQuery.data
        bot.sendMessage(
            ChatId.fromId(chatId),
            Texts.photoDescriptionTemplate(photo),
            replyMarkup = photoMenuButton
        )
    }
    private val callbackQueryList = listOf(
        ::callbackQueryBack,
        ::callbackQueryAddNewPhotos,
        ::callbackQueryPhotoList,
        ::callbackQueryPhotoUpdateAuthor,
        ::callbackQueryPhotoDelete,
        ::callbackQueryYes,
        ::callbackQueryNo,
        ::callbackQuery
    )

    fun initCallbackQuery(dispatcher: Dispatcher) {
        callbackQueryList.forEach { callbackQuery -> callbackQuery(dispatcher) }
    }
}
