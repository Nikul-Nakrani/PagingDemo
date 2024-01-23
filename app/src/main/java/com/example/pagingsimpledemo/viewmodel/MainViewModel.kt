package com.example.pagingsimpledemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pagingsimpledemo.data.Result
import com.example.pagingsimpledemo.repository.MainRepository

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()

    fun getMovieList(): LiveData<PagingData<Result>> {
        return mainRepository.getAllMovies().cachedIn(viewModelScope)
    }
}