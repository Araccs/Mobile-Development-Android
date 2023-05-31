package fi.tamk.dummyjson

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.JsonObject

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
}

private fun getNextUserId(userList: List<GetAll.User>): Int {
    val maxId = userList.maxOfOrNull { it.id } ?: 0
    return maxId + 1
}

@Composable
fun UserListScreen(userList: List<GetAll.User>) {
    val filteredUserList = remember { mutableStateOf(userList) }
    var editedUser by remember { mutableStateOf<GetAll.User?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    val nextUserId = getNextUserId(filteredUserList.value)
                    editedUser = GetAll.User(nextUserId, "", "", 0)
                    // Create a new user with default values
                    filteredUserList.value += editedUser!! // Add the new user to the list
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add User")
            }

            LazyColumn(Modifier.weight(1f)) {
                items(filteredUserList.value) { user ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Id: ${user.id}")
                            Text(text = "First Name: ${user.firstName}")
                            Text(text = "Last Name: ${user.lastName}")
                            Text(text = "Age: ${user.age}")
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth()) {
                                IconButton(icon = Icons.Default.Delete, onClick = {
                                    filteredUserList.value =
                                        filteredUserList.value.filterNot { it.id == user.id }
                                }, modifier = Modifier.padding(end = 8.dp))

                                IconButton(icon = Icons.Default.Edit, onClick = {
                                    editedUser = user
                                })
                            }
                        }
                    }
                }
            }
        }

        if (editedUser != null) {
            EditUserScreen(user = editedUser!!, onUserUpdated = { updatedUser ->
                // Update the user data in the userList
                val updatedList = filteredUserList.value.map { if (it.id == updatedUser.id) updatedUser else it }
                filteredUserList.value = updatedList

                // Clear the editedUser state variable
                editedUser = null
            })
        }
    }
}

@Composable
fun IconButton(icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(onClick = onClick, modifier = modifier) {
        Image(
            imageVector = icon,
            contentDescription = null, // Provide a meaningful description if needed
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun EditUserScreen(user: GetAll.User, onUserUpdated: (GetAll.User) -> Unit) {
    // Define mutable state variables for each user property you want to edit
    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var age by remember { mutableStateOf(user.age) }
    // Add more mutable state variables for other user properties

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set the background color of the screen
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the user data and allow editing
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") }
            )
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") }
            )
            TextField(
                value = age.toString(),
                onValueChange = { age = it.toIntOrNull() ?: 0 },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


            Button(
                onClick = {
                    // Create a new user object with the entered data
                    val newUser = GetAll.User(
                        id = user.id,
                        firstName = firstName,
                        lastName = lastName,
                        age = age

                    )
                    onUserUpdated(newUser) // Pass the new user data back to the UserListScreen
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Accept")
            }
        }
    }
}



