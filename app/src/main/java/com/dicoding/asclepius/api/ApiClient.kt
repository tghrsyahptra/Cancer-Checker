package com.dicoding.asclepius.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * ApiClient merupakan objek singleton yang digunakan untuk mengonfigurasi dan menyediakan
 * instance Retrofit untuk melakukan komunikasi dengan server.
 */
object ApiClient {

    // Membuat instance Retrofit dengan konfigurasi dasar
    private val retrofit: Retrofit = Retrofit.Builder()
        // Mengatur base URL untuk komunikasi API
        .baseUrl(ApiConfig.BASE_URL)
        // Menambahkan converter untuk mengonversi response JSON ke objek Kotlin
        .addConverterFactory(GsonConverterFactory.create())
        // Membangun instance Retrofit
        .build()

    /**
     * Menyediakan instance NewsApiService untuk melakukan request ke API.
     * Instance ini digunakan untuk mengakses endpoint yang ada di server.
     */
    val newsApiService: NewsApiService = retrofit.create(NewsApiService::class.java)
}