package com.example.krantikari

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.krantikari.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load data when app first opens
        callApis()

        binding.button.setOnClickListener {
            callApis()
        }
    }

    private fun callApis() {
        // Disable button while loading
        binding.button.isEnabled = false
        binding.button.text = getString(R.string.btn_loading)

        lifecycleScope.launch {

            // --- api1: Chuck Norris Joke → textView3 ---
            try {
                val jokeResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.api1.getJokes()
                }
                if (jokeResponse.isSuccessful) {
                    val joke = jokeResponse.body()?.value ?: "No joke found"
                    Log.d(TAG, "Joke: $joke")
                    binding.textView3.text = joke
                } else {
                    Log.e(TAG, "api1 error: ${jokeResponse.code()}")
                    binding.textView3.text = "Couldn't load joke (${jokeResponse.code()})"
                }
            } catch (e: Exception) {
                Log.e(TAG, "api1 exception: ${e.message}")
                binding.textView3.text = "Network error: ${e.message}"
            }

            // --- api2: Random User → textView4 + imageView ---
            try {
                val userResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.api2.getUser()
                }
                if (userResponse.isSuccessful) {
                    val result = userResponse.body()?.results?.firstOrNull()
                    if (result != null) {
                        val fullName = "${result.name.title} ${result.name.first} ${result.name.last}"
                        Log.d(TAG, "User: $fullName")

                        // Name in textView4
                        binding.textView4.text = fullName

                        // Robohash avatar in imageView
                        val roboUrl = "https://robohash.org/${result.login.username}"
                        Log.d(TAG, "RoboHash URL: $roboUrl")
                        Glide.with(this@MainActivity)
                            .load(roboUrl)
                            .circleCrop()
                            .into(binding.imageView)
                    }
                } else {
                    Log.e(TAG, "api2 error: ${userResponse.code()}")
                    binding.textView4.text = "Couldn't load user (${userResponse.code()})"
                }
            } catch (e: Exception) {
                Log.e(TAG, "api2 exception: ${e.message}")
                binding.textView4.text = "Network error: ${e.message}"
            }

            // Re-enable button after both calls finish
            binding.button.isEnabled = true
            binding.button.text = getString(R.string.btn_next)
        }
    }
}