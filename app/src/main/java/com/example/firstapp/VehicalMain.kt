package com.example.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.Adapters.VehicleAdapter
import com.example.firstapp.Database.DatabaseHelper
import com.example.firstapp.LogBookModels.Vehicle

class VehicalMain : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var vehicleList:ArrayList<Vehicle>
    lateinit var vehicleAdapter: VehicleAdapter
    var db = DatabaseHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehical_main)
        val fab: View = findViewById(R.id.floatingActionButtonNewVehicle)
        recyclerView = findViewById(R.id.recycler_view_holder)
        vehicleList = ArrayList()

        fab.setOnClickListener { view ->
            var intent = Intent(this, NewVehicle::class.java)
            startActivity(intent)
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this,3)
        var data = db.getAllVehicles()
        while (data.moveToNext()){
            var response = Vehicle(data.getInt(0),data.getString(1),data.getString(2),data.getInt(3),data.getInt(4))
            vehicleList.add(response)
        }
         var listener = object : VehicleAdapter.OnItemClickListener {
            override fun onItemClick(vehicle: Vehicle) {
                var intent = Intent(applicationContext, VehicleLogs::class.java)
                intent.putExtra("vId", vehicle.vehicleId)
                intent.putExtra("plate", vehicle.plate.toString())
                intent.putExtra("target", vehicle.target)
                startActivity(intent)
            }
        }

        var listener2 = object : VehicleAdapter.OnItemLongClickLister {
            override fun onItemLongClick(vehicle: Vehicle): Boolean {
                var intent = Intent(applicationContext, VehicleModifier::class.java)
                intent.putExtra("vID", vehicle.vehicleId)
                intent.putExtra("vPlateNumber", vehicle.plate)
                intent.putExtra("vTargetAmount", vehicle.target)
                intent.putExtra("vCapacity", vehicle.capacity)
                intent.putExtra("vRoute", vehicle.route)
                startActivity(intent)
                return true
            }

        }
        vehicleAdapter = VehicleAdapter(vehicleList, listener,listener2)

        recyclerView.adapter = vehicleAdapter
    }


}