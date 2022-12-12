package com.example.githubuserapp.ui.detail.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.data.response.DetailUserResponse
import com.example.githubuserapp.data.database.FavoriteUser
import com.example.githubuserapp.data.repository.FavoriteRepository
import com.example.githubuserapp.ui.events.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application): ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _user = MutableLiveData<DetailUserResponse>()
    val user: LiveData<DetailUserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun findUserDetail(userName: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(userName)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.postValue(response.body())
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
                _snackbarText.value = Event("onFailure: ${t.message}")
            }
        })
    }

    suspend fun checkUser(id: Int): Int = mFavoriteRepository.checkUser(id)

    fun addToFavorite(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            mFavoriteRepository.insert(favoriteUser)
            _snackbarText.value = Event("${favoriteUser.login} berhasil ditambahkan ke daftar favorite")
        }
    }

    fun removeFromFavorite(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            mFavoriteRepository.delete(favoriteUser.id)
            _snackbarText.value = Event("${favoriteUser.login} berhasil dihapus dari daftar favorite")
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }

}