package testtask.telegrambot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.extensions.filters.Filter
import mu.KotlinLogging
import testtask.telegrambot.bot.*
import testtask.telegrambot.bot.callback.ConfigureCallbackQuery
import testtask.telegrambot.bot.constant.BotConfig
import testtask.telegrambot.bot.constant.Texts
import testtask.telegrambot.bot.database.Firestore
import testtask.telegrambot.bot.database.model.UserPhoto
import testtask.telegrambot.bot.http.HttpClient


fun main() {
    val logger = KotlinLogging.logger { }
    Firestore
    Waiting
    val bot = bot {
        token = BotConfig.API_TOKEN

        dispatch {
            command("start") {
                bot.sendMessage(ChatId.fromId(message.chat.id), Texts.onboarding_message)
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = Texts.main_manu,
                    replyMarkup = mainMenuButtons
                )
            }

            message(Filter.Text and IsLink.not()) {
                if(!Waiting.waitingUpdate && Waiting.waitingPhoto.isBlank()) return@message
                Firestore.update(Waiting.waitingPhoto, message.text!!)
                Waiting.waitingUpdate = false
                bot.sendMessage(
                    ChatId.fromId(message.chat.id),
                    Texts.photo_autor_name_updated
                )
            }
            message(IsLink) {
                if (!Waiting.waitingEnterText) return@message
                val url = message.text ?: return@message
                val chatId = message.chat.id
                try {
                    val photoList = HttpClient.getData(url)
                    val userPhotoList = photoList.map { UserPhoto(chatId, "${chatId}_${it.id}", it) }
                    userPhotoList.forEach(Firestore::save)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    logger.error(e.message, e)
                }
                Waiting.waitingEnterText = false
                bot.sendMessage(ChatId.fromId(message.chat.id), Texts.photos_saved)
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = Texts.main_manu,
                    replyMarkup = mainMenuButtons
                )
            }
//            callbackQuery("back") {
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                waiting.waitingPhoto = ""
//                waiting.waitingDelete = false
//                waiting.waitingUpdate = false
//                waiting.waitingEnterText = false
//                bot.sendMessage(
//                    chatId = ChatId.fromId(chatId),
//                    text = Texts.main_manu,
//                    replyMarkup = mainMenuButtons
//                )
//            }
//            callbackQuery("addNewPhotos") {
//                waiting.waitingEnterText = true
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                bot.sendMessage(
//                    ChatId.fromId(chatId),
//                    Texts.add_new_photos_message,
//                    replyMarkup = backButton
//                )
//            }
//            callbackQuery("photoList") {
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                val photoList = Firestore.findById(chatId)
//                val buttons = generatePhotoButtons(photoList)
//                bot.sendMessage(
//                    chatId = ChatId.fromId(chatId),
//                    text = Texts.photo_list,
//                    replyMarkup = buttons
//                )
//            }
//            callbackQuery("photoUpdateAuthor") {
//                if(waiting.waitingPhoto.isBlank()) return@callbackQuery
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                bot.sendMessage(
//                    ChatId.fromId(chatId),
//                    Texts.photo_enter_author_name
//                )
//                waiting.waitingUpdate = true
//            }
//            callbackQuery("photoDelete") {
//                if(waiting.waitingPhoto.isBlank()) return@callbackQuery
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                waiting.waitingDelete = true
//                bot.sendMessage(
//                    chatId = ChatId.fromId(chatId),
//                    text = Texts.photo_delete_confirmation_template,
//                    replyMarkup = confirmationButton
//                )
//            }
//            callbackQuery("yes") {
//                if(waiting.waitingPhoto.isBlank() && !waiting.waitingDelete) return@callbackQuery
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                Firestore.delete(waiting.waitingPhoto)
//                waiting.waitingPhoto = ""
//                waiting.waitingDelete = false
//                bot.sendMessage(
//                    chatId = ChatId.fromId(chatId),
//                    text = Texts.main_manu,
//                    replyMarkup = mainMenuButtons
//                )
//            }
//            callbackQuery("no") {
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                waiting.waitingDelete = false
//                bot.sendMessage(
//                    chatId = ChatId.fromId(chatId),
//                    text = Texts.main_manu,
//                    replyMarkup = mainMenuButtons
//                )
//            }
//            callbackQuery {
//                if(waiting.waitingDelete || waiting.waitingUpdate) return@callbackQuery
//                val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
//                if (notPhotoButtons.contains(callbackQuery.data)) return@callbackQuery
//                val photo = Firestore.findByUserIdAndPhotoId(callbackQuery.data)?.photo
//                if(photo == null) {
//                    bot.sendMessage(ChatId.fromId(chatId), "none photo")
//                    return@callbackQuery
//                }
////                val imageUrl = ImageUtils.validateUrl(photo.downloadUrl!!)?.let {
////                    bot.sendMediaGroup(
////                        chatId = ChatId.fromId(chatId),
////                        mediaGroup = MediaGroup.from(
////                            InputMediaPhoto(
////                                media = TelegramFile.ByUrl(it),
////                                caption = photo.author!!
////                            )
////                        ))
////                }
//                waiting.waitingPhoto = callbackQuery.data
//                bot.sendMessage(
//                    ChatId.fromId(chatId),
//                    Texts.photoDescriptionTemplate(photo),
//                    replyMarkup = photoMenuButton
//                )
//            }
            ConfigureCallbackQuery.initCallbackQuery(this)
        }
    }
    bot.startPolling()
}
