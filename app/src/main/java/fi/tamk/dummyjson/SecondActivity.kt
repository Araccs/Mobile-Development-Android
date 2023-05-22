package fi.tamk.dummyjson
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the userData from the intent extras
        val userData = intent.getStringExtra("userData")

        // TODO: Parse the userData JSON string to retrieve the userList array

        // TODO: Display the userList in the second activity
    }
}