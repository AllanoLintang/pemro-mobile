package com.allano.alquran.data

import android.util.Log
import com.allano.alquran.data.local.AyahDao
import com.allano.alquran.data.local.AyahEntity
import com.allano.alquran.data.local.SurahDao
import com.allano.alquran.data.local.SurahEntity
import com.allano.alquran.data.model.MergedAyah
import com.allano.alquran.data.model.Surah
import com.allano.alquran.data.network.ApiClient
import kotlinx.coroutines.flow.Flow

class SurahRepository(private val surahDao: SurahDao, private val ayahDao: AyahDao) {


    fun getSurahsStream(): Flow<List<SurahEntity>> = surahDao.getAllSurahs()

    suspend fun refreshSurahs() {
        try {
            Log.d("SurahRepository", "Mencoba mengambil data dari network...")
            val response = ApiClient.apiService.getAllSurahs()

            if (response.isSuccessful) {
                val surahsFromApi = response.body()?.data ?: emptyList()
                Log.d("SurahRepository", "Sukses mengambil ${surahsFromApi.size} surah dari API")
                if (surahsFromApi.isNotEmpty()) {
                    val surahEntities = surahsFromApi.map { it.toEntity() }
                    surahDao.insertAll(surahEntities)
                    Log.d("SurahRepository", "Data berhasil disimpan ke Room.")
                }
            } else {
                Log.e("SurahRepository", "API Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("SurahRepository", "Network Error: ${e.message}", e)
        }
    }
    fun getAyahStream(surahNumber: Int): Flow<List<AyahEntity>> {
        return ayahDao.getAyahsBySurahNumber(surahNumber)
    }
    suspend fun refreshSurahDetail(surahNumber: Int) {
        try {
            val response = ApiClient.apiService.getSurahDetailWithMultipleEditions(surahNumber)
            if (response.isSuccessful) {
                val responseData = response.body()?.data
                val arabicEdition = responseData?.getOrNull(0)
                val englishEdition = responseData?.getOrNull(1)

                if (arabicEdition != null && englishEdition != null) {
                    val mergedAyahs = arabicEdition.ayahs.zip(englishEdition.ayahs).map { (arabicAyah, englishAyah) ->
                        AyahEntity(
                            number = arabicAyah.number,
                            surahNumber = surahNumber,
                            numberInSurah = arabicAyah.numberInSurah,
                            arabicText = arabicAyah.text,
                            englishText = englishAyah.text
                        )
                    }
                    ayahDao.insertAll(mergedAyahs)
                    Log.d("SurahRepository", "Detail ayat untuk surah $surahNumber disimpan ke DB.")
                }
            }
        } catch (e: Exception) {
            Log.e("SurahRepository", "Gagal me-refresh detail surah: ", e)
        }
    }
    suspend fun getSurahDetailFromApi(surahNumber: Int): List<MergedAyah>? {
        Log.d("RepoDetail", "Memulai pengambilan detail untuk surah: $surahNumber")
        return try {
            val response = ApiClient.apiService.getSurahDetailWithMultipleEditions(surahNumber)
            Log.d("RepoDetail", "Panggilan API berhasil (isSuccessful): ${response.isSuccessful}")

            if (response.isSuccessful) {
                val responseData = response.body()?.data
                Log.d("RepoDetail", "Jumlah edisi yang diterima dari API: ${responseData?.size}")

                val arabicEdition = responseData?.getOrNull(0)
                val englishEdition = responseData?.getOrNull(1)
                Log.d("RepoDetail", "Edisi Arab ditemukan: ${arabicEdition != null}")
                Log.d("RepoDetail", "Edisi Inggris ditemukan: ${englishEdition != null}")

                if (arabicEdition != null && englishEdition != null) {
                    val mergedList = arabicEdition.ayahs.zip(englishEdition.ayahs).map { (arabicAyah, englishAyah) ->
                        MergedAyah(
                            numberInSurah = arabicAyah.numberInSurah,
                            arabicText = arabicAyah.text,
                            englishText = englishAyah.text
                        )
                    }
                    Log.d("RepoDetail", "Berhasil menggabungkan ${mergedList.size} ayat.")
                    return mergedList
                } else {
                    Log.e("RepoDetail", "Salah satu atau kedua edisi (Arab/Inggris) tidak ditemukan dalam respons API.")
                    return null
                }
            } else {
                Log.e("RepoDetail", "Panggilan API tidak sukses. Kode: ${response.code()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("RepoDetail", "Terjadi EXCEPTION saat mengambil detail: ", e)
            return null
        }
    }
    private fun Surah.toEntity(): SurahEntity {
        return SurahEntity(
            number = this.number,
            name = this.name,
            englishName = this.englishName,
            englishNameTranslation = this.englishNameTranslation,
            revelationType = this.revelationType,
            numberOfAyahs = this.numberOfAyahs
        )
    }
}