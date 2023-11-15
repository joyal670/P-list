package com.iroid.healthdomain.data

import java.math.BigInteger
import java.security.MessageDigest
import java.lang.StringBuilder
import java.security.NoSuchAlgorithmException


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
/*    val MD5 = "MD5"
    val hexString = StringBuilder()
    try {
        // Create MD5 Hash
        val digest = MessageDigest
            .getInstance(MD5)
        //digest.update(digest.getBytes())
        val messageDigest = digest.digest()

        // Create Hex String

        for (aMessageDigest in messageDigest) {
            var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2) h = "0$h"
            hexString.append(h)
        }

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return hexString.toString()*/
}