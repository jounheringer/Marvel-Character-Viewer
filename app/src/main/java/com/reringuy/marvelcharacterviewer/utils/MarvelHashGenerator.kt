package com.reringuy.marvelcharacterviewer.utils

import java.security.MessageDigest

fun generateMarvelHash(timestamp: String, privateKey: String, publicKey: String): String {
    val valueToHash = timestamp + privateKey + publicKey
    val md5Instance = MessageDigest.getInstance("MD5")
    return md5Instance.digest(valueToHash.toByteArray())
        .joinToString("") {
            "%02x".format(it)
        }
}