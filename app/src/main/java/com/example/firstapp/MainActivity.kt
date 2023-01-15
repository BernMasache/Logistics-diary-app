package com.example.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var imageView = findViewById<ImageView>(R.id.imageView)
        val vehicleBtn = findViewById<Button>(R.id.vehicleNavigatorBtn)
        rotateLogo(imageView)
        vehicleBtn.setOnClickListener{
            val intent = Intent(this, VehicalMain::class.java)
            startActivity(intent)
        }

        val statisticsBtn = findViewById<Button>(R.id.statisticsNavigatorBtn)
        statisticsBtn.setOnClickListener{
            val intent = Intent(this, StatisticsMain::class.java)
            startActivity(intent)
        }
    }

    fun rotateLogo(image:ImageView){
            val clk_rotate = AnimationUtils.loadAnimation(this,R.anim.rotate_logo)
            image.startAnimation(clk_rotate)
    }
}
