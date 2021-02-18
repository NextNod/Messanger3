package com.example.messanger3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun onRegister(view: View)
    {
        val intent = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(intent)
    }

    public fun onLogin(view :View)
    {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}