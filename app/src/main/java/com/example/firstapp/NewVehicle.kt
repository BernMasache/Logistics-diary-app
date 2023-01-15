package com.example.firstapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.firstapp.Database.DatabaseHelper
import com.example.firstapp.LogBookModels.Vehicle
import com.google.android.material.textfield.TextInputEditText

class NewVehicle : AppCompatActivity() {
    var plateNumber:String=""
    var vehicleTarget:Int=0
    var vehicleCapacity:Int=0
    var vehicleRoute:String=""
    var newVehicletextEdit = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_vehicle)
        plateNumber = getIntent().getStringExtra("plateNum").toString()
        vehicleRoute = getIntent().getStringExtra("rout").toString()
        vehicleCapacity = getIntent().getIntExtra("capaci",0)
        vehicleTarget = getIntent().getIntExtra("targetAmoun",0)
        newVehicletextEdit = getIntent().getStringExtra("vehicleEdit").toString()

        val create = findViewById<Button>(R.id.createVehicleBtn)
        val plate = findViewById<TextInputEditText>(R.id.vehiclePlate)
        val route = findViewById<TextInputEditText>(R.id.vehicleRoute)
        val target = findViewById<TextInputEditText>(R.id.vehicleTarget)
        val capacity = findViewById<TextInputEditText>(R.id.vehicleCapacity)

        val db = DatabaseHelper(this)

        create.setOnClickListener {
            if ( plate.text.toString().isEmpty() && route.text.toString().isEmpty() && capacity.text.toString().isEmpty() && target.text.toString().isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val vehicle = Vehicle(
                    plate.text.toString().replace(" ",""),
                    route.text.toString().replace(" ",""),
                    Integer.parseInt(capacity.text.toString().replace(" ","")),
                    Integer.parseInt(target.text.toString().replace(" ",""))
                )

                db.insertVehicle(this,vehicle)
            }
        }

    }

}