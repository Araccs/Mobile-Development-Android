package fi.tamk.dummyjson

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp { userData ->
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra("userData", userData)
                startActivity(intent)
            }
        }
    }
}

@Composable
fun MyApp(onGetUserData: (String) -> Unit) {
    var textValue by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    // When the "Get all user data" button is clicked, call the getData function of
                    // the GetAll class
                    GetAll().getData { userData ->
                        // Pass the received user data to the onGetUserData callback
                        onGetUserData(userData)
                    }
                },
                modifier = Modifier.size(100.dp)
            ) {
                Text("Get all user data")
            }
            Spacer(modifier = Modifier.height(120.dp))

            OutlinedTextField(
                value = textValue,
                onValueChange = { newValue ->
                    // Update the textValue variable with the new value entered in the text field
                    textValue = newValue
                },
                label = { Text("Enter search term here") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.LightGray,
                    focusedIndicatorColor = Color.Green,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    // When the "Search users" button is clicked, call the getSearchData function
                    // of the GetBySearch class
                    GetBySearch().getSearchData(textValue) { userData ->
                        // Pass the received user data to the onGetUserData callback
                        onGetUserData(userData)
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .width(120.dp)
            ) {
                Text("Search users")
            }
        }
    }
}






