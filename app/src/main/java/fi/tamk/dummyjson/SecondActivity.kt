package fi.tamk.dummyjson
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the userData from the intent extras
        val userData = intent.getStringExtra("userData")

        val gson = Gson()
        val responseJson = gson.fromJson(userData, JsonObject::class.java) as JsonObject

        val usersJsonArray = responseJson.getAsJsonArray("users")
        val userList = mutableListOf<GetAll.User>()

        usersJsonArray?.let {
            for (jsonElement in it) {
                val userJson = jsonElement.asJsonObject
                val user = gson.fromJson(userJson, GetAll.User::class.java)
                userList.add(user)
            }
        }

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray)
            ) {
                UserListScreen(userList = userList)
            }
        }
    }

@Composable
fun UserListScreen(userList: List<GetAll.User>) {
    LazyColumn {
        items(userList) { user ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "First Name: ${user.firstName}")
                    Text(text = "Last Name: ${user.lastName}")
                    Text(text = "Age: ${user.age}")
                    Spacer(modifier = Modifier.height(10.dp))
                    // Add more Text elements for other properties as needed
                }
            }
        }
    }
}
}