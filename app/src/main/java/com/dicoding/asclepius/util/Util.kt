package com.dicoding.asclepius.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Mengonversi timestamp (dalam format Long) menjadi string dengan format yang telah ditentukan.
 * @param timestamp Waktu dalam bentuk timestamp (milliseconds).
 * @return String yang berisi tanggal dan waktu dalam format "yyyy-MM-dd HH:mm:ss".
 */
fun parseTimestamp(timestamp: Long): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(timestamp)
    return simpleDateFormat.format(date)
}

/**
 * Mengonversi string timestamp dari satu format ke format lain.
 * @param timestampString String yang berisi timestamp dalam format input yang diberikan.
 * @param inputFormat Format input timestamp yang ingin diproses, default "yyyy-MM-dd'T'HH:mm:ss'Z'".
 * @param outputFormat Format output yang diinginkan, default "yyyy-MM-dd HH:mm:ss".
 * @return String hasil konversi ke format output yang diinginkan.
 */
fun convertTimestampToString(
    timestampString: String,
    inputFormat: String = "yyyy-MM-dd'T'HH:mm:ss'Z'",
    outputFormat: String = "yyyy-MM-dd HH:mm:ss",
): String {
    // Membuat objek SimpleDateFormat untuk input dan output format yang diinginkan
    val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
    val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())

    // Set timezone input untuk mengatasi format UTC pada timestamp yang masuk
    inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")

    return try {
        // Parsing timestampString ke objek Date menggunakan input format
        val parsedDate = inputDateFormat.parse(timestampString)
        // Mengubah objek Date ke format output yang diinginkan dan mengembalikannya sebagai String
        outputDateFormat.format(parsedDate!!)
    } catch (e: Exception) {
        // Menangani kesalahan parsing
        e.printStackTrace()
        // Mengembalikan string kosong jika terjadi error
        ""
    }
}