package com.example.githubuserapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert
    suspend fun addToFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * from FavoriteUser ORDER BY ID ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT COUNT(*) FROM FavoriteUser WHERE id = :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM FavoriteUser WHERE id = :id")
    suspend fun removeFromFavorite(id: Int)
}