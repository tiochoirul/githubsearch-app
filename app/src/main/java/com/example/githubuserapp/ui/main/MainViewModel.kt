package com.example.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuserapp.data.response.ItemsItem
import com.example.githubuserapp.data.response.UserResponse
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.ui.events.Event
import com.example.githubuserapp.ui.setting.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Event<Boolean>>()
    val isLoading: LiveData<Event<Boolean>> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun findUser(query: String) {
        _isLoading.value = Event(true)
        val client = ApiConfig.getApiService().getUserList(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = Event(false)
                if (response.isSuccessful) {
                    _listUser.postValue(response.body()?.items)
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = Event(false)
                Log.d(TAG, "onFailure: ${t.message}")
                _snackbarText.value = Event("onFailure: ${t.message}")
            }
        })
    }

    fun getUsers() {
        _isLoading.value = Event(true)
        val client = ApiConfig.getApiService().getAllUser()
        client.enqueue(object : Callback<ArrayList<ItemsItem>> {
            override fun onResponse(
                call: Call<ArrayList<ItemsItem>>,
                response: Response<ArrayList<ItemsItem>>
            ) {
                _isLoading.value = Event(false)
                if (response.isSuccessful) {
                    _listUser.postValue(response.body())
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                _isLoading.value = Event(false)
                Log.d(TAG, "onFailure: ${t.message}")
                _snackbarText.value = Event("onFailure: ${t.message}")
            }
        })
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}