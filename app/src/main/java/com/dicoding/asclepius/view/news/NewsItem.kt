package com.dicoding.asclepius.view.news

/**
 * Data model untuk item berita yang berisi informasi mengenai judul,
 * URL gambar, dan URL artikel berita.
 *
 * @property title Judul berita
 * @property imageUrl URL gambar terkait berita
 * @property url URL lengkap ke artikel berita (opsional)
 */
data class NewsItem(
    val title: String,     // Menyimpan judul berita
    val imageUrl: String,  // Menyimpan URL gambar berita
    val url: String?       // Menyimpan URL artikel berita (opsional)
)