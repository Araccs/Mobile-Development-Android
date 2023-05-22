package fi.tamk.dummyjson

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class GetBySearch {


    data class User(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val email: String,
        // Add other properties as needed
    )

    fun getSearchData(textValue: String) {
        val client = OkHttpClient()
        val url = "https://dummyjson.com/users/search?q=$textValue"

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
                    val userList = mutableListOf<GetAll.User>()

                    usersJsonArray?.let {
                        for (jsonElement in it) {
                            val userJson = jsonElement.asJsonObject
                            val user = gson.fromJson(userJson, GetAll.User::class.java)
                            userList.add(user)
                        }
                    }

                    println(userList)

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