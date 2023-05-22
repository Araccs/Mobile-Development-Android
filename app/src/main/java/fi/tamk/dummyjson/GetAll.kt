package fi.tamk.dummyjson

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.google.gson.JsonArray
import okhttp3.*
import java.io.IOException
import com.google.gson.JsonObject

class GetAll {
    data class User(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val email: String,
        // Add other properties as needed
    )

    fun getData(){
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
                    val responseJson = gson.fromJson(jsonResponse, JsonObject::class.java)

                    val usersJsonArray = responseJson.getAsJsonArray("users")
                    val userList = mutableListOf<User>()

                    usersJsonArray?.let {
                        for (jsonElement in it) {
                            val userJson = jsonElement.asJsonObject
                            val user = gson.fromJson(userJson, User::class.java)
                            userList.add(user)
                            return userList
                        }
                    }






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