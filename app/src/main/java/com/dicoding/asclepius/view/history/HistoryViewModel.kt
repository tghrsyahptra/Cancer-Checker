package com.dicoding.asclepius.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.ClassificationResult
import com.dicoding.asclepius.repository.ResultRepository
import kotlinx.coroutines.launch

/**
 * HistoryViewModel bertugas untuk mengelola data hasil prediksi dan mengatur alur
 * pengambilan data dari repository.
 */
class HistoryViewModel(private val repository: ResultRepository) : ViewModel() {

    // LiveData untuk mengamati perubahan data hasil klasifikasi
    private val _results = MutableLiveData<List<ClassificationResult>>()
    val results: LiveData<List<ClassificationResult>> = _results

    /**
     * Inisialisasi ViewModel, memulai pemuatan data hasil prediksi dari repository.
     */
    init {
        loadResults()
    }

    /**
     * Fungsi untuk memuat data hasil klasifikasi dari repository menggunakan coroutine.
     * Mengambil data secara asinkron dan memperbarui LiveData.
     */
    private fun loadResults() {
        viewModelScope.launch {
            fetchDataFromRepository()
        }
    }

    /**
     * Fungsi untuk mengambil data dari repository dan memperbarui nilai LiveData.
     * Menggunakan observeForever untuk mendapatkan hasil dari repository.
     */
    private suspend fun fetchDataFromRepository() {
        repository.getAll().observeForever { results ->
            _results.postValue(results)
        }
    }
}