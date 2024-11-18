package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.databases.AppDao
import com.dicoding.asclepius.data.local.ClassificationResult

/**
 * Repository untuk mengelola data hasil klasifikasi, berinteraksi dengan DAO untuk operasi CRUD.
 */
class ResultRepository private constructor(private val dao: AppDao) {

    /**
     * Mendapatkan semua data hasil klasifikasi yang disimpan dalam database.
     * @return LiveData yang berisi daftar hasil klasifikasi.
     */
    fun getAll(): LiveData<List<ClassificationResult>> {
        return dao.getAll()
    }

    /**
     * Mendapatkan hasil klasifikasi berdasarkan ID.
     * @param id ID hasil klasifikasi yang ingin diambil.
     * @return LiveData yang berisi hasil klasifikasi sesuai dengan ID yang diberikan.
     */
    fun getById(id: Int): LiveData<ClassificationResult> {
        return dao.getClassificationResultById(id)
    }

    /**
     * Menyisipkan hasil klasifikasi baru ke dalam database.
     * @param classificationResult Objek hasil klasifikasi yang akan disimpan.
     */
    suspend fun insert(classificationResult: ClassificationResult) {
        dao.insert(classificationResult)
    }

    /**
     * Menghapus hasil klasifikasi dari database.
     * @param classificationResult Objek hasil klasifikasi yang akan dihapus.
     */
    suspend fun delete(classificationResult: ClassificationResult) {
        dao.delete(classificationResult)
    }

    companion object {
        @Volatile
        private var instance: ResultRepository? = null

        /**
         * Mendapatkan instance singleton dari ResultRepository.
         * @param dao Objek DAO yang digunakan untuk operasi database.
         * @return Instance ResultRepository yang dikelola secara singleton.
         */
        fun getInstance(dao: AppDao): ResultRepository =
            instance ?: synchronized(this) {
                // Membuat instance baru jika belum ada
                instance ?: ResultRepository(dao).also { instance = it }
            }
    }
}