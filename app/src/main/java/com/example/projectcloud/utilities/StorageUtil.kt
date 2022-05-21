package com.example.projectcloud.utilities

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class StorageUtil {
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

private val currentUserRef: StorageReference
    get() = storageInstance.reference
        .child(
            FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null.")
        )

fun uploadProfilePhoto(
    imageBytes: ByteArray,
    onSuccess: (imagePath: String) -> Unit
) {
    val ref = currentUserRef.child("profilePictures/${UUID.nameUUIDFromBytes(imageBytes)}")
    ref.putBytes(imageBytes)
        .addOnSuccessListener {
            onSuccess(ref.path)
        }
}

fun uploadMessageImage(
    imageBytes: ByteArray,
    onSuccess: (imagePath: String) -> Unit
) {
    val ref = currentUserRef.child("messages/${UUID.nameUUIDFromBytes(imageBytes)}")
    ref.putBytes(imageBytes)
        .addOnSuccessListener {

            it.metadata!!.reference!!.downloadUrl.addOnCompleteListener {
                onSuccess(it.result.toString())
            }
//                onSuccess(it.task.getResult().toString())

            //                onSuccess(ref.downloadUrl.toString())
        }
}

fun pathToReference(path: String) = storageInstance.getReference(path)
}