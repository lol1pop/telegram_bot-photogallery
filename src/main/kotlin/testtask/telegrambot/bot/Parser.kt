package testtask.telegrambot.bot

import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.extensions.filters.Filter
import java.net.URL

object IsLink: Filter {
    override fun Message.predicate(): Boolean = text != null && try {
        URL(text)
        true
    }catch(e: Exception) {
        false
    }
}
