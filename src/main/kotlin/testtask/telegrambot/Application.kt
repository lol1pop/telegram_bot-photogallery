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
            ConfigureCallbackQuery.initCallbackQuery(this)
        }
    }
    bot.startPolling()
}
