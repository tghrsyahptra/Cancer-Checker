package com.dicoding.asclepius.api

import com.dicoding.asclepius.BuildConfig

/**
 * ApiConfig berfungsi sebagai tempat penyimpanan konfigurasi API yang digunakan
 * dalam aplikasi, seperti base URL dan API key.
 */
object ApiConfig {

    // Base URL untuk mengakses API, diambil dari konfigurasi di BuildConfig
    const val BASE_URL = BuildConfig.NEWS_API_URL

    // API key untuk otentikasi akses API, juga diambil dari BuildConfig
    const val API_KEY = BuildConfig.NEWS_API_KEY
}