package com.example.pagingsimpledemo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pagingsimpledemo.network.RetrofitService
import com.example.pagingsimpledemo.data.Result

class MoviePagingSource(private val apiService: RetrofitService) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val position = params.key ?: 1
            val response =
                apiService.getTopRatedMovies("e8d648003bd11b5c498674fbd4905525", "en-US", position)
            LoadResult.Page(
                data = response.body()!!.results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

        /*  if (state.anchorPosition != null) {
           val anchorPage = state.closestPageToPosition(state.anchorPosition!!)
           if (anchorPage?.prevKey != null) {
               return anchorPage.prevKey!!.plus(1)
           } else if (anchorPage?.nextKey != null) {
               return anchorPage.nextKey!!.minus(1)
           }
       } else {
           return null
       }*/
    }

}