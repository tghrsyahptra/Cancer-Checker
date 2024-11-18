package com.dicoding.asclepius.view.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel untuk mengelola data berita kesehatan.
 * Bertanggung jawab untuk mengambil data dari repositori dan menyediakan data tersebut ke UI.
 */
class NewsViewModel : ViewModel() {

    // Menyimpan referensi ke repositori berita
    private val newsRepository = NewsRepository()

    // LiveData untuk menyimpan dan mengamati daftar berita
    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> = _newsList

    // LiveData untuk mengamati status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Mengambil data berita kesehatan dari repositori dan mengupdate LiveData.
     * Memanggil fungsi repositori untuk mengambil data dan memperbarui UI setelah data diterima.
     */
    fun fetchHealthNews() {
        // Menandakan bahwa proses pengambilan data sedang berlangsung
        _isLoading.postValue(true)

        // Memanggil repositori untuk mengambil berita kesehatan
        newsRepository.getHealthNews(
            onSuccess = { newsList ->
                // Mengupdate LiveData dengan daftar berita yang diterima
                _newsList.postValue(newsList)

                // Menyembunyikan indikator loading setelah data diterima
                _isLoading.postValue(false)
            },
            onFailure = { errorMessage ->
                // Menyembunyikan indikator loading jika terjadi error
                _isLoading.postValue(false)

                // Bisa menambahkan penanganan error lebih lanjut (misalnya, menampilkan pesan kesalahan)
            }
        )
    }
}