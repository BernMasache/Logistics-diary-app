package com.example.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LogBookMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_book_main)
        val fab: View = findViewById(R.id.floatingActionButtonNewLog)

        fab.setOnClickListener { view ->
            var intent = Intent(this, CreateLogs::class.java)
            startActivity(intent)
        }
    }
}