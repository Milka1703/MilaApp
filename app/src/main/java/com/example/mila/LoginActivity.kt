package com.example.mila

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mila.SupabaseClientSingleton.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email_inp = emailEditText.text.toString()
            val password_inp = passwordEditText.text.toString()

            lifecycleScope.launch {
                try {
                    val result = supabase.from("users").select {
                        filter {
                            eq("email", email_inp)
                            eq("password", password_inp)
                        }
                    }.decodeList<User>()

                    if (result.isNotEmpty()) {
                        Log.d("Login", "User logged in successfully")
                        Toast.makeText(this@LoginActivity, "User logged in successfully", Toast.LENGTH_SHORT).show()

                        // Переход на другую активность
                        val intent = Intent(this@LoginActivity, MainScreenActivity::class.java)
                        intent.putExtra("username", result[0].username)
                        startActivity(intent)
                    } else {
                        Log.e("Login", "Invalid login credentials")
                        Toast.makeText(this@LoginActivity, "Invalid login credentials", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("Login", "Error logging in user: ${e.message}")
                    Toast.makeText(this@LoginActivity, "Error logging in user: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
