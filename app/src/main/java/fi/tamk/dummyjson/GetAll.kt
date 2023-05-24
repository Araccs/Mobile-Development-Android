package fi.tamk.dummyjson

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class GetAll {
    data class User(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val email: String,
        // Add other properties as needed
    )

    fun getData(onDataReceived: (String) -> Unit) {
        val client = OkHttpClient()
        val url = "https://dummyjson.com/users"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val gson = Gson()
                    val jsonResponse = response.body?.string()
                        ?: "" // Provide a default value for null response body
                    onDataReceived(jsonResponse) // Pass the response to the callback
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle network errors
            }
        })
    }
}