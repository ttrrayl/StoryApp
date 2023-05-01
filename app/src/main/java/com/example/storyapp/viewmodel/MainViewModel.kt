package com.example.storyapp.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.storyapp.data.local.UserModel
import com.example.storyapp.data.local.UserSession
import com.example.storyapp.data.remote.ApiConfig
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.data.remote.response.MainResponse
import com.example.storyapp.ui.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (private val pref: UserSession) : ViewModel(){

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): LiveData<UserModel>{
        return pref.getToken().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStories("Bearer $token")
        client.enqueue(object : Callback<MainResponse>{
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG,"onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responBody = response.body()
                    if (responBody != null){
                        _listStory.value = responBody.listStory
                    }
                } else{
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }


}