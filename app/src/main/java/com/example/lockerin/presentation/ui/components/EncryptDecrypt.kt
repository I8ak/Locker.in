package com.example.lockerin.presentation.ui.components

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.compose.runtime.Composable
import java.security.SecureRandom
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.content.Context


private const val AES_KEY_ALIAS = "MySensitiveDataKeyAlias"

fun generateAesKey(context: Context): SecretKey {
    try {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null) // Carga el Keystore (no necesita password para AndroidKeyStore)

        // Intenta obtener la clave por su alias
        val existingKey = keyStore.getKey(AES_KEY_ALIAS, null)

        if (existingKey != null && existingKey is SecretKey) {
            return existingKey // La clave ya existe, la devolvemos
        }

        // Si la clave no existe, la generamos
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            AES_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        val newKey = keyGenerator.generateKey()

        return newKey

    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}


fun generateIv(): IvParameterSpec {
    val iv = ByteArray(16)
    SecureRandom().nextBytes(iv)
    return IvParameterSpec(iv)
}

fun encrypt(data: String, key: SecretKey, iv: IvParameterSpec): Pair<String, String> {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)

    val dataBytes = data.toByteArray(Charsets.UTF_8)
    val encryptedBytes = cipher.doFinal(dataBytes)

    val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    val ivBase64 = Base64.encodeToString(iv.iv, Base64.NO_WRAP)

    return Pair(encryptedBase64, ivBase64)
}

fun decrypt(encryptedBase64: String, ivBase64: String, key: SecretKey): String {
    val encryptedBytes = Base64.decode(encryptedBase64, Base64.NO_WRAP)
    val ivBytes = Base64.decode(ivBase64, Base64.NO_WRAP)
    val iv = IvParameterSpec(ivBytes)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, iv)

    val decryptedBytes = cipher.doFinal(encryptedBytes)
    val numCard=String(decryptedBytes, Charsets.UTF_8)
    return "**** ${numCard.takeLast(4)}"
}