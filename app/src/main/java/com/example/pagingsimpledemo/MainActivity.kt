package com.example.pagingsimpledemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingsimpledemo.network.RetrofitService
import com.example.pagingsimpledemo.paging.MoviePagerAdapter
import com.example.pagingsimpledemo.repository.MainRepository
import com.example.pagingsimpledemo.viewmodel.MainViewModel
import com.example.pagingsimpledemo.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private val adapter = MoviePagerAdapter(this)
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressDialog: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        prepareRecyclerView()

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(mainRepository)
        )[MainViewModel::class.java]

        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        adapter.addLoadStateListener { loadState ->
            // show empty list
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            )
                progressDialog.isVisible = true
            else {
                progressDialog.isVisible = false
                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getMovieList().observe(this@MainActivity) {
                it?.let {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }

    private fun prepareRecyclerView() {
        recyclerView.adapter = adapter
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerview)
        progressDialog = findViewById(R.id.progressBar)
    }
}