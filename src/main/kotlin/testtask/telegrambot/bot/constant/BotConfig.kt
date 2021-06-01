package testtask.telegrambot.bot.constant

object BotConfig {
    const val BOT_NAME = "Simple Photo Gallery Test Task"
    var API_TOKEN = System.getenv("TG_BOT_API_TOKEN") ?: throw IllegalArgumentException("Please Provide a valid bot token via Env: TG_BOT_API_TOKEN")
}

