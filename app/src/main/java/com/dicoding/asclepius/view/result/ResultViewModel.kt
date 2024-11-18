package com.dicoding.asclepius.view.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.ClassificationResult
import com.dicoding.asclepius.repository.ResultRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk mengelola data hasil klasifikasi.
 * Menyediakan fungsi untuk menyimpan dan menghapus hasil klasifikasi melalui repository.
 */
class ResultViewModel(private val repository: ResultRepository) : ViewModel() {

    /**
     * Menyimpan hasil klasifikasi ke dalam database menggunakan repository.
     * Operasi ini dijalankan di dalam scope coroutine.
     *
     * @param classificationResult Hasil klasifikasi yang ingin disimpan.
     */
    fun insert(classificationResult: ClassificationResult) {
        // Menjalankan operasi insert pada repository dalam scope coroutine
        viewModelScope.launch {
            repository.insert(classificationResult)
        }
    }

    /**
     * Menghapus hasil klasifikasi dari database menggunakan repository.
     * Operasi ini dijalankan di dalam scope coroutine.
     *
     * @param classificationResult Hasil klasifikasi yang ingin dihapus.
     */
    fun delete(classificationResult: ClassificationResult) {
        // Menjalankan operasi delete pada repository dalam scope coroutine
        viewModelScope.launch {
            repository.delete(classificationResult)
        }
    }
}