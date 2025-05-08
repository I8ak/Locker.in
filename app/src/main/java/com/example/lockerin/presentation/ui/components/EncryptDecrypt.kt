package com.example.lockerin.presentation.ui.components

import androidx.compose.runtime.Composable
import java.security.SecureRandom
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


fun generateAesKey(): SecretKeySpec {
    val keyGenerator = KeyGenerator.getInstance("AES")
    keyGenerator.init(256)
    val secretKey = keyGenerator.generateKey()
    return SecretKeySpec(secretKey.encoded, "AES")
}


fun generateIv(): IvParameterSpec {
    val iv = ByteArray(16)
    SecureRandom().nextBytes(iv)
    return IvParameterSpec(iv)
}

fun encrypt(data: String, key: SecretKeySpec, iv: IvParameterSpec): Pair<String, String> {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)

    val dataBytes = data.toByteArray(Charsets.UTF_8)
    val encryptedBytes = cipher.doFinal(dataBytes)

    val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    val ivBase64 = Base64.encodeToString(iv.iv, Base64.NO_WRAP)

    return Pair(encryptedBase64, ivBase64)
}

fun decrypt(encryptedBase64: String, ivBase64: String, key: SecretKeySpec): String {
    val encryptedBytes = Base64.decode(encryptedBase64, Base64.NO_WRAP)
    val ivBytes = Base64.decode(ivBase64, Base64.NO_WRAP)
    val iv = IvParameterSpec(ivBytes)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, iv)

    val decryptedBytes = cipher.doFinal(encryptedBytes)
    val numCard=String(decryptedBytes, Charsets.UTF_8)
    return "**** ${numCard.takeLast(4)}"
}