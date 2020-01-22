//package com.example.filepicker
//
//import android.text.TextUtils
//import android.util.Log
//import java.io.*
//import java.lang.RuntimeException
//import java.math.BigInteger
//import java.security.MessageDigest
//import java.security.NoSuchAlgorithmException
//
//class CheckSum {
//
//    private val TAG = "MD5"
//
//    fun checkMD5KT(md5: String, updateFile: File): Boolean {
//
//        if (TextUtils.isEmpty(md5) || updateFile == null) {
//            Log.e(TAG, "MD5 string empty or updateFile is null")
//            return false
//        }
//
//        val calculatedDigest = calculateMD5KT(updateFile)
//        if (calculatedDigest == null) {
//            Log.e(TAG, "calculatedDigest is null")
//            return false
//        }
//
//        Log.v(TAG, "Calculated digest: " + calculatedDigest)
//        Log.v(TAG, "Provided digest: " + md5)
//
//        return calculatedDigest.equalsIgnoreCase(md5)
//    }
//
//    fun calculateMD5KT(updateFile: File) {
//        val digest: MessageDigest
//
//        try {
//            digest = MessageDigest.getInstance("MD5")
//        } catch (e: NoSuchAlgorithmException) {
//            Log.e(TAG, "Exception while getting digest", e)
//            return null
//        }
//
//        val inputStream: InputStream
//        try {
//            inputStream = FileInputStream(updateFile)
//        } catch(e: FileNotFoundException) {
//            Log.e(TAG, "Exception while getting FileInputStream", e)
//            return null
//        }
//
//        val buffer = ByteArray(8192)
//        val read = inputStream.read(buffer)
//        try {
//            while (read > 0) {
//                digest.update(buffer, 0, read)
//            }
//            val md5sum: ByteArray = digest.digest()
//            val bigInteger = BigInteger(1, md5sum)
//            var output = bigInteger.toString(16)
//            // Fill to 32 chars
//            output = String.format("%32s", output).replace(' ', '0')
//
//            return output
//        } catch(e: IOException) {
//            throw RuntimeException("Unable to process file for MD5", e)
//        } finally {
//            try {
//                inputStream.close()
//            } catch (e: IOException) {
//                Log.e(TAG, "Exception on closing MD5 input stream", e)
//            }
//        }
//    }
//}