package com.example.mila

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mila.SupabaseClientSingleton.supabase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

object SupabaseClientSingleton {
    val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://wlukldufwcibysoevbbq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndsdWtsZHVmd2NpYnlzb2V2YmJxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjg1NzU4NjcsImV4cCI6MjA0NDE1MTg2N30.HW0tKsmn3lwHRJZ5IgPW7CF4zc2m2B0oFnHAQHtcN0Y"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }

    val auth = supabase.auth
}

@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val email: String,
    val password: String,
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                try {
                    val user = User(username = username, email = email, password = password)
                    val result = supabase.from("users").insert(user).decodeSingle<User>()
                    Log.d("Register", "User registered successfully: $result")
                } catch (e: Exception) {
                    Log.e("Register", "Error registering user: ${e.message}")
                }
            }
        }
        loginButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}
