package com.dicoding.asclepius.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface NewsApiService berfungsi untuk mendefinisikan endpoint
 * yang tersedia dalam API berita, dalam hal ini digunakan untuk mencari
 * berita kesehatan berdasarkan kriteria pencarian tertentu.
 */
interface NewsApiService {

    /**
     * Fungsi ini digunakan untuk mendapatkan berita kesehatan berdasarkan
     * kata kunci pencarian, kategori, bahasa, dan API key yang diberikan.
     * Endpoint yang dipanggil adalah "top-headlines" untuk mengambil berita terbaru.
     *
     * @param query Kata kunci pencarian untuk berita
     * @param category Kategori berita, misalnya "health"
     * @param language Bahasa berita, misalnya "en" untuk bahasa Inggris
     * @param apiKey Kunci API untuk otentikasi
     * @return Call objek yang akan mengembalikan respons dalam bentuk NewsResponse
     */
    @GET("top-headlines")
    fun searchHealthNews(
        @Query("q") query: String,            // Kata kunci pencarian berita
        @Query("category") category: String,   // Kategori berita (misalnya kesehatan)
        @Query("language") language: String,   // Bahasa yang diinginkan untuk berita
        @Query("apiKey") apiKey: String       // Kunci API untuk otentikasi
    ): Call<NewsResponse>
}