package fi.tamk.dummyjson

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class GetBySearch {
    data class User(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val email: String,
    )

    fun getSearchData(textValue: String, onDataReceived: (String) -> Unit) {
        val client = OkHttpClient()
        val url = "https://dummyjson.com/users/search?q=$textValue"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Create an instance of Gson for JSON parsing
                    val gson = Gson()
                    // Get the response body as a string, providing a default
                    // empty string if it's null
                    val jsonResponse = response.body?.string() ?: ""
                    // Pass the JSON response to the onDataReceived callback
                    onDataReceived(jsonResponse)
                } else {
                    // Handle unsuccessful response
                    print("Unsuccessful response: ${response.code}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle network errors
                print("Network error: ${e.message}")
            }
        })
    }
}













