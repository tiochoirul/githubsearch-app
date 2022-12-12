package com.example.githubuserapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserapp.data.database.FavoriteUser
import com.example.githubuserapp.data.database.FavoriteUserDao
import com.example.githubuserapp.data.database.FavoriteUserRoomDatabase

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteUserDao

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteDao.getAllFavoriteUser()

    suspend fun checkUser(id: Int): Int = mFavoriteDao.checkUser(id)

    suspend fun insert(favoriteUser: FavoriteUser) {
        mFavoriteDao.addToFavorite(favoriteUser)
    }

    suspend fun delete(id: Int) {
        mFavoriteDao.removeFromFavorite(id)
    }
}