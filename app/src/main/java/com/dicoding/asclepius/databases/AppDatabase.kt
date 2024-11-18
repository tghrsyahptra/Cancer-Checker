package com.dicoding.asclepius.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.ClassificationResult

/**
 * Database Room untuk aplikasi Asclepius yang mengelola entitas ClassificationResult.
 * Database ini menyediakan akses melalui DAO untuk melakukan operasi pada tabel classificationresult.
 */
@Database(entities = [ClassificationResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Mengembalikan instance AppDao untuk berinteraksi dengan database.
     *
     * @return DAO untuk mengakses data pada tabel classificationresult.
     */
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Mengambil instance singleton dari database AppDatabase.
         * Jika instance sudah ada, akan langsung dikembalikan. Jika belum, akan dibuat baru.
         *
         * @param context Context aplikasi untuk mengakses database.
         * @return Instance dari database AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "db_asclepius"  // Mengganti nama database menjadi db_asclepius
                ).build().also { INSTANCE = it }
            }
        }
    }
}