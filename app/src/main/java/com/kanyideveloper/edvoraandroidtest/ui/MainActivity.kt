package com.kanyideveloper.edvoraandroidtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.kanyideveloper.edvoraandroidtest.adapters.AllProductsAdapter
import com.kanyideveloper.edvoraandroidtest.databinding.ActivityMainBinding
import com.kanyideveloper.edvoraandroidtest.util.Resource
import com.kanyideveloper.edvoraandroidtest.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.view.View
import android.widget.AdapterView

import androidx.core.content.ContextCompat
import com.kanyideveloper.edvoraandroidtest.R
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val allProductsAdapter by lazy {
        AllProductsAdapter()
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.main_dark)
        // set status background black
        val decorView: View = window.decorView
        //set status text  light
        decorView.systemUiVisibility =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()

        setUpObserver()

        viewModel.spinnerProducts.observe(this, Observer { products ->
            Timber.d(products.toString())
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, products)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.productsSpinner.adapter = arrayAdapter
        })

        viewModel.spinnerStates.observe(this, Observer { states ->
            Timber.d(states.toString())
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.statesSpinner.adapter = arrayAdapter
        })

        /*viewModel.spinnerProducts.observe(this, Observer { products ->
            Timber.d(products.toString())
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, products)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.productsSpinner.adapter = arrayAdapter
        })*/

        binding.statesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                
            }

        }

        binding.filters.setOnClickListener {
            binding.filterCard.isVisible = !binding.filterCard.isVisible
        }
    }

    private fun setUpObserver() {
        viewModel.prod.observe(this, { result ->
            when (result) {
                is Resource.Success -> {
                    binding.productsProgressBar.isVisible = false

                    if (result.data?.isEmpty()!!) {
                        //showSnackbar("Nothing Found, Try Again!")
                    } else {
                        allProductsAdapter.submitList(result.data)
                        binding.allProductsRecycler.adapter = allProductsAdapter
                    }
                }

                is Resource.Failure -> {
                    binding.productsProgressBar.isVisible = false
                    //showSnackbar(result.message ?: "Unknown Error!")
                }

                is Resource.Loading -> {
                    binding.productsProgressBar.isVisible = true
                }
            }
        })
    }
}