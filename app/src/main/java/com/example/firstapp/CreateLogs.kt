package com.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.firstapp.Database.DatabaseHelper
import com.example.firstapp.LogBookModels.Vehicle
import com.example.firstapp.LogBookModels.VehicleLog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var dateHolder = ""
var plateHolder = 0
class CreateLogs : AppCompatActivity() {
    var vehicles = ArrayList<String>()
    var db = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_logs)
        var setDate = findViewById<Button>(R.id.setDate)
        var spinner = findViewById<Spinner>(R.id.mySpinner)
        var amountValue = findViewById<TextInputEditText>(R.id.amountValue)
        var createLogBtn = findViewById<Button>(R.id.createLogBtn)
        var data = db.getAllVehicles()

        while (data.moveToNext()){
            var hh = Vehicle(data.getInt(0),data.getString(1),data.getString(2),data.getInt(3),data.getInt(4))
            vehicles.add(hh.plate)
        }
        var arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,vehicles)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                plateHolder = vehicles[position].toInt()

//                Toast.makeText(applicationContext, "Selected : "+plateHolder, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        setDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                dateHolder = date
                setDate.setText(dateHolder.toString())
//                Toast.makeText(this, "$date is selected", Toast.LENGTH_LONG).show()

                // Setting up the event for when cancelled is clicked
                datePicker.addOnNegativeButtonClickListener {
//                    Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG)
//                        .show()
                }
                // Setting up the event for when back button is pressed
                datePicker.addOnCancelListener {
//                    Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
                }
            }
        }

        createLogBtn.setOnClickListener {
            if ( amountValue.text.toString().isEmpty() && dateHolder.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                var vehicleLog = VehicleLog(
                    Integer.parseInt(amountValue.text.toString()),
                    dateHolder.toString(),
                    Integer.parseInt(plateHolder.toString())
                )

                db.insertLogs(this, vehicleLog)
            }
        }

    }



}


