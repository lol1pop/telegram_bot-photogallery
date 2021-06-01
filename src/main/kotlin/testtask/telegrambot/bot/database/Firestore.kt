package testtask.telegrambot.bot.database

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import testtask.telegrambot.bot.database.model.UserPhoto
import java.io.FileInputStream

object Firestore {
    private val db: Firestore
    val collection: CollectionReference
    init {
        val serviceAccount = FileInputStream(Firestore::class.java.getResource("/simple-photo-gallery-test-task-firebase-adminsdk-6zoce-b7b1a4a211.json").path)
        val googleCred = GoogleCredentials.fromStream(serviceAccount)
        val options = FirebaseOptions.builder()
            .setCredentials(googleCred)
            .build()
        FirebaseApp.initializeApp(options)
        db = FirestoreClient.getFirestore()
        collection = db.collection("photos")
    }

    fun findAll(): List<UserPhoto> {
        val result = arrayListOf<UserPhoto>()
        collection
            .get()
            .get().documents.apply {
                forEach { doc ->
                    val newUserPhoto = doc.toObject(UserPhoto::class.java)
                    result.add(newUserPhoto)
                }
            }
        return result
    }

    fun findByUserIdAndPhotoId(userId: String): UserPhoto? {
        collection.whereEqualTo("userId", userId)
            .get()
            .get()
            .documents.apply {
                forEach { doc ->
                    return doc.toObject(UserPhoto::class.java)
                }
            }
        return null
    }

    fun findById(id: Long): List<UserPhoto> {
        val result = arrayListOf<UserPhoto>()
        collection.whereEqualTo("id", id)
            .get()
            .get()
            .documents.apply {
                forEach { doc ->
                    val newUserPhoto = doc.toObject(UserPhoto::class.java)
                    result.add(newUserPhoto)
                }
            }
        return result
    }

    fun save(userPhoto: UserPhoto) {
        collection.document().set(userPhoto, SetOptions.merge())
    }

    fun delete(userId: String) {
        collection.whereEqualTo("userId", userId).get()
            .get()
            .documents.apply {
                forEach { doc ->
                    collection.document(doc.id).delete()
                    return
                }
            }
    }

    fun update(userId: String, newAuthor: String) {
        collection.whereEqualTo("userId", userId).get()
            .get()
            .documents.apply {
                forEach { doc ->
                    collection.document(doc.id).update(FieldPath.of("photo", "author"), newAuthor)
                    return
                }
            }
    }
}

