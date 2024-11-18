package com.dicoding.asclepius.view.news

import com.dicoding.asclepius.api.ApiClient
import com.dicoding.asclepius.api.ApiConfig
import com.dicoding.asclepius.api.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository untuk mengelola data berita terkait kesehatan dengan mengakses API.
 * Repository ini berfungsi untuk mendapatkan berita dengan memanggil API eksternal.
 */
class NewsRepository {

    /**
     * Mengambil berita kesehatan dari API.
     * @param onSuccess Callback yang dipanggil saat pengambilan data berhasil.
     * @param onFailure Callback yang dipanggil saat terjadi kegagalan dalam pengambilan data.
     */
    fun getHealthNews(
        onSuccess: (List<NewsItem>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Memanggil API untuk mendapatkan berita dengan kata kunci "cancer"
        ApiClient.newsApiService.searchHealthNews("cancer", "health", "en", ApiConfig.API_KEY)
            .enqueue(object : Callback<NewsResponse> {

                /**
                 * Menangani respons sukses dari API.
                 * Mengonversi respons menjadi daftar berita dan mengirimkannya ke callback onSuccess.
                 * @param call Objek panggilan API.
                 * @param response Respons yang diterima dari API.
                 */
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        // Mengambil daftar artikel dari respons API
                        val articles = response.body()?.articles ?: emptyList()

                        // Memetakan artikel ke objek NewsItem, hanya jika data valid
                        val newsList = articles.mapNotNull { article ->
                            // Validasi apakah artikel memiliki judul, gambar, dan URL yang lengkap
                            if (!article.title.isNullOrEmpty() && !article.urlToImage.isNullOrEmpty() && !article.url.isNullOrEmpty()) {
                                NewsItem(article.title, article.urlToImage, article.url) // Membuat objek NewsItem
                            } else {
                                null // Jika data artikel tidak lengkap, abaikan
                            }
                        }
                        // Menyampaikan hasil berita ke callback onSuccess
                        onSuccess(newsList)
                    } else {
                        // Jika API gagal merespons dengan sukses, panggil onFailure
                        onFailure("Failed to fetch news")
                    }
                }

                /**
                 * Menangani kegagalan saat memanggil API.
                 * Menyampaikan pesan kesalahan ke callback onFailure.
                 * @param call Objek panggilan API.
                 * @param t Throwable yang mewakili kesalahan.
                 */
                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    // Jika terjadi kesalahan, panggil onFailure dengan pesan error
                    onFailure(t.message ?: "Unknown error")
                }
            })
    }
}