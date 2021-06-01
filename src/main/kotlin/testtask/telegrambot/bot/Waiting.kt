package testtask.telegrambot.bot

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

object Waiting {
    private val _waitingEnterText = AtomicBoolean(false)
    var waitingEnterText: Boolean
        get() = _waitingEnterText.get()
        set(value) = _waitingEnterText.set(value)

    private val _waitingDelete = AtomicBoolean(false)
    var waitingDelete: Boolean
        get() = _waitingDelete.get()
        set(value) = _waitingDelete.set(value)

    private val _waitingUpdate = AtomicBoolean(false)
    var waitingUpdate: Boolean
        get() = _waitingUpdate.get()
        set(value) = _waitingUpdate.set(value)

    private val _waitingPhoto = AtomicReference<String>("")
    var waitingPhoto: String
        get() = _waitingPhoto.get()
        set(value) = _waitingPhoto.set(value)
}
