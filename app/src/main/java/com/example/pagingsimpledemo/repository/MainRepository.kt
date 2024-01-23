package com.example.pagingsimpledemo.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.pagingsimpledemo.network.RetrofitService
import com.example.pagingsimpledemo.data.Result
import com.example.pagingsimpledemo.paging.MoviePagingSource

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getAllMovies(): LiveData<PagingData<Result>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                enablePlaceholders = false,
                initialLoadSize = 2
            ), pagingSourceFactory = {
                MoviePagingSource(retrofitService)
            }, initialKey = 1
        ).liveData
    }
}