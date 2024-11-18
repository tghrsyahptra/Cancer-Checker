package com.dicoding.asclepius.api

import com.google.gson.annotations.SerializedName

/**
 * Data class yang mewakili respons dari API berita.
 *
 * @param status Status dari respons API (misalnya "ok" atau "error").
 * @param totalResults Jumlah total hasil berita yang ditemukan.
 * @param articles Daftar artikel yang dikembalikan oleh API.
 */
data class NewsResponse(
    @SerializedName("status") val status: String,                 // Status dari respons API
    @SerializedName("totalResults") val totalResults: Int,        // Total hasil berita
    @SerializedName("articles") val articles: List<Article>       // Daftar artikel
)

/**
 * Data class yang mewakili artikel berita dalam respons API.
 *
 * @param source Sumber berita (misalnya nama situs atau organisasi).
 * @param author Penulis berita (opsional, bisa null).
 * @param title Judul artikel.
 * @param description Deskripsi singkat artikel (opsional, bisa null).
 * @param url URL ke artikel penuh.
 * @param urlToImage URL gambar terkait artikel (opsional, bisa null).
 * @param publishedAt Waktu publikasi artikel.
 * @param content Konten artikel (opsional, bisa null).
 */
data class Article(
    @SerializedName("source") val source: Source,                // Sumber berita
    @SerializedName("author") val author: String?,                // Penulis artikel (opsional)
    @SerializedName("title") val title: String,                  // Judul artikel
    @SerializedName("description") val description: String?,      // Deskripsi artikel (opsional)
    @SerializedName("url") val url: String,                      // URL artikel
    @SerializedName("urlToImage") val urlToImage: String?,        // URL gambar artikel (opsional)
    @SerializedName("publishedAt") val publishedAt: String,      // Waktu publikasi
    @SerializedName("content") val content: String?               // Konten artikel (opsional)
)

/**
 * Data class yang mewakili sumber dari artikel berita.
 *
 * @param id ID dari sumber berita (opsional, bisa null).
 * @param name Nama dari sumber berita.
 */
data class Source(
    @SerializedName("id") val id: String?,                      // ID sumber berita (opsional)
    @SerializedName("name") val name: String                     // Nama sumber berita
)