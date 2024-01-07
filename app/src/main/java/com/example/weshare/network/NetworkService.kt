package com.example.weshare.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class NetworkService {

    suspend fun makeApiCall(urlString: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val connection = url.openConnection()
                connection.connectTimeout = 10000 // 10 seconds
                connection.readTimeout = 10000

                val stream = connection.getInputStream()
                val result = stream.bufferedReader().use { it.readText() }
                stream.close()

                result
            } catch (e: IOException) {
                Log.e("NetworkService", "Error making API call: ${e.message}")
                null
            }
        }
    }

    // Add more methods for different types of network requests, like POST, PUT, DELETE, etc.

}
