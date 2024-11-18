package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.databases.AppDatabase
import com.dicoding.asclepius.repository.ResultRepository

/**
 * Kelas Injection berfungsi untuk menyediakan dependensi berupa repository
 * yang akan digunakan oleh berbagai komponen dalam aplikasi.
 * Kelas ini menggunakan pola singleton untuk memastikan hanya ada satu instance repository
 * yang digunakan di seluruh aplikasi.
 */
object Injection {

    /**
     * Menyediakan instance dari ResultRepository.
     * Fungsi ini akan mengakses database melalui AppDatabase dan mengembalikan ResultRepository
     * yang telah diinisialisasi dengan AppDao.
     *
     * @param context adalah context aplikasi yang digunakan untuk mendapatkan instance database.
     * @return instance dari ResultRepository yang telah dikonfigurasi.
     */
    fun provideRepository(context: Context): ResultRepository {
        // Mendapatkan instance database dari AppDatabase
        val database = AppDatabase.getDatabase(context)

        // Mendapatkan DAO (Data Access Object) untuk melakukan operasi database
        val dao = database.appDao()

        // Mengembalikan instance ResultRepository yang telah dibuat
        return ResultRepository.getInstance(dao)
    }
}