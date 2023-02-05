package com.example.news

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.R
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val TAG: String = "Main"

class MainActivity : AppCompatActivity() {

    lateinit var adapter : NewsAdapter
    private lateinit var binding : ActivityMainBinding

    private lateinit var countrySelection: String
    private lateinit var categorySelection: String
    private lateinit var recyclerAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.tvBlank.isVisible = false
        val countryList = listOf("in", "us", "ae", "ar", "at", "hk", "ch", "jp", "fr", "ru", "sa",
                        "kr", "rs", "de", "gb")
        val pageList = listOf("1", "2")

        populateSpinner(binding.spCountry, countryList, binding.tvCountrySelected)
        populateSpinner(binding.spPage, pageList, binding.tvPageSelected)

        binding.btnFetch.setOnClickListener{

            if(binding.tvCountrySelected.text!=null && binding.tvPageSelected.text!=null){
                getNews()
            } else {
                Toast.makeText(this@MainActivity, "Above fields cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateSpinner(spinner: Spinner, list: List<String>, txtView: TextView){

        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                txtView.text = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "You selected ${parent?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO fill later
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getNews(){

        binding.pbBar.isVisible = true
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsInterface::class.java)

        GlobalScope.launch(Dispatchers.IO){
            val response = api.getHeadlines(binding.tvCountrySelected.text.toString(), binding.tvPageSelected.text.toString().toInt())
            if(response.isSuccessful){

                Log.d(TAG, response.body().toString())

                withContext(Dispatchers.Main){
                    if(response.body().toString().isNotEmpty()){
                        setRecycler(response.body()!!)
                        binding.pbBar.isVisible = false
                    } else {
                        binding.pbBar.isVisible = false
                        binding.tvBlank.isVisible = true
                        binding.tvBlank.text = "No news to fetch"
                    }
                }
            } else {
                Log.d(TAG, "Error")
            }
        }
    }

    private fun setRecycler(news: News){
        recyclerAdapter = NewsAdapter(this@MainActivity, news.articles)
        binding.rvNewsList.adapter = recyclerAdapter
        binding.rvNewsList.layoutManager = LinearLayoutManager(this@MainActivity)
    }
}