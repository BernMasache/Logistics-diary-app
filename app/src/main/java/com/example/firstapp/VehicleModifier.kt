package com.example.firstapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firstapp.Database.DatabaseHelper
import com.google.android.material.textfield.TextInputEditText

class VehicleModifier : AppCompatActivity() {
    var plate:String=""
    var target:Int=0
    var capacity:Int=0
    var route:String=""
    var vehicleId:Int=0
    val db = DatabaseHelper(this)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_modifier)
        plate = getIntent().getStringExtra("vPlateNumber").toString()
        route = getIntent().getStringExtra("vRoute").toString()
        capacity = getIntent().getIntExtra("vCapacity",0).toInt()
        vehicleId = getIntent().getIntExtra("vID",0).toInt()
        target = getIntent().getIntExtra("vTargetAmount",0).toInt()

        var vehiclePlate = findViewById<TextInputEditText>(R.id.vehiclePlateNumber)
        var vehicleCapacity = findViewById<TextInputEditText>(R.id.vehicleCapacityValue)
        var vehicleRoute = findViewById<TextInputEditText>(R.id.vehicleRoute)
        var vehicleTarget = findViewById<TextInputEditText>(R.id.vehicleTargetValue)
        var update_vehicle = findViewById<Button>(R.id.update_vehicle)
        var deleteVehicle = findViewById<Button>(R.id.deleteVehicle)

        vehiclePlate.setText(plate)
        vehicleCapacity.setText(capacity.toString())
        vehicleRoute.setText(route)
        vehicleTarget.setText(target.toString())

        update_vehicle.setOnClickListener{
            updateVehicleIntent(vehicleId,vehiclePlate.text.toString().replace(" ",""),Integer.parseInt(vehicleTarget.text.toString().replace(" ","")),Integer.parseInt(vehicleCapacity.text.toString().replace(" ","")),vehicleRoute.text.toString().replace(" ",""))

        }

        deleteVehicle.setOnClickListener{view:View->
            var builder = AlertDialog.Builder(this)
            builder.setTitle("Delete : "+plate)
            builder.setMessage("Are you sure you want to permanently delete this vehicle?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes"){
                dialogInterface,which->
                run {
                    var res_logs = db.deleteVehicleLogs(vehicleId)
                    if (res_logs==1){
                        var res:Int = db.deleteVehicle(vehicleId)
                        if (res==1){
                            Toast.makeText(applicationContext,"Deleted successfully",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext,VehicalMain::class.java))
                        }
                    }else{
                        var res:Int = db.deleteVehicle(vehicleId)
                        if (res==1){
                            Toast.makeText(applicationContext,"Deleted successfully",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext,VehicalMain::class.java))
                        }
                    }

                }

            }

            builder.setNegativeButton("No"){
                    dialogInterface,which->
                run {
//                    Toast.makeText(applicationContext,"Deleted successfully",Toast.LENGTH_SHORT).show()
                }

            }

            var alert:AlertDialog=builder.create()
            alert.setCancelable(false)
            alert.show()
        }


    }

    fun updateVehicleIntent(id:Int, p:String,t:Int,c:Int,r:String){

        var res = db.updateVehicle(applicationContext,id,p,t,c,r)
        if (res==1){
            startActivity(Intent(applicationContext,VehicalMain::class.java))
        }else{

        }

    }

}