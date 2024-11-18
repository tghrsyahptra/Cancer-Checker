package com.dicoding.asclepius.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.repository.ResultRepository
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.view.history.HistoryViewModel
import com.dicoding.asclepius.view.news.NewsViewModel
import com.dicoding.asclepius.view.result.ResultViewModel

/**
 * Factory untuk menyediakan instance ViewModel dengan akses ke ResultRepository.
 * ViewModelFactory bertanggung jawab untuk membuat ViewModel dengan memberikan dependensi yang diperlukan.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val repository: ResultRepository) :
    ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        /**
         * Mengembalikan instance ViewModelFactory, membuatnya jika belum ada.
         * @param context Konteks aplikasi yang digunakan untuk menyediakan repository.
         * @return Instance ViewModelFactory.
         */
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }

    /**
     * Fungsi untuk membuat ViewModel sesuai dengan tipe yang diminta.
     * @param modelClass Kelas ViewModel yang ingin dibuat.
     * @return Instance ViewModel yang sesuai dengan modelClass.
     * @throws IllegalArgumentException Jika modelClass tidak dikenal.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Memeriksa tipe ViewModel yang diminta dan mengembalikan instance yang sesuai
        return when {
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel() as T
            }
            else -> {
                // Jika ViewModel tidak dikenal, lemparkan pengecualian
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}