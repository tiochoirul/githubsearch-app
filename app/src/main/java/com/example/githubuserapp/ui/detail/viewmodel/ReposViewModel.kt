package com.example.githubuserapp.ui.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.data.response.RepositoryResponseItem
import com.example.githubuserapp.ui.events.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReposViewModel: ViewModel() {

    private val _repository = MutableLiveData<ArrayList<RepositoryResponseItem>>()
    val repository: LiveData<ArrayList<RepositoryResponseItem>> = _repository

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getRepository(userName: String) {
        _isLoading.value = true
            val client = ApiConfig.getApiService().getRepositoryUser(userName)
        client.enqueue(object : Callback<ArrayList<RepositoryResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<RepositoryResponseItem>>,
                response: Response<ArrayList<RepositoryResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _repository.postValue(response.body())
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<RepositoryResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message}")
                _snackbarText.value = Event("onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "RepositoryViewModel"
    }

}