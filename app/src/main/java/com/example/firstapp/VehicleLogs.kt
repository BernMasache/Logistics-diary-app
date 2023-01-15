package com.example.firstapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.Adapters.VehicleLogsAdapter
import com.example.firstapp.Database.DatabaseHelper
import com.example.firstapp.LogBookModels.VehicleLog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VehicleLogs : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var vehicleLogsList:ArrayList<VehicleLog>
    lateinit var vehicleLogsAdapter: VehicleLogsAdapter
    var db = DatabaseHelper(this)
    var plate:String = ""
    var target:Int = 0
    var id:Int=0
    var dateHolder = ""
    var vehicleId:Int=0

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_logs)
        plate = getIntent().getStringExtra("plate").toString()
        target = getIntent().getIntExtra("target",0).toInt()
        vehicleId = getIntent().getIntExtra("vId",0).toInt()

        recyclerView = findViewById(R.id.vehicle_logs_recyclerview)
        vehicleLogsList = ArrayList()

        var targetHolder = findViewById<TextView>(R.id.targetId)
        var plateHolder = findViewById<TextView>(R.id.plateId)
        var createLogBtnId = findViewById<Button>(R.id.createLogBtnId)
        var amountValueId = findViewById<TextInputEditText>(R.id.amountValueId)
        var dateBtnId = findViewById<Button>(R.id.dateBtnId)

        targetHolder.setText("Target : Mk"+target.toString())
        plateHolder.setText("Plate No. : "+plate.toString())

        dateBtnId.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val date = dateFormatter.format(Date(it))
                dateHolder = date
                dateBtnId.setText(dateHolder.toString())
                // Setting up the event for when cancelled is clicked
                datePicker.addOnNegativeButtonClickListener {
                }
                // Setting up the event for when back button is pressed
                datePicker.addOnCancelListener {
                }
            }
        }


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        var data = attachAdapter()
        while (data.moveToNext()){
            var response = VehicleLog(data.getInt(0), data.getInt(1),data.getString(2),data.getInt(3), target)
            vehicleLogsList.add(response)
        }

        vehicleLogsAdapter = VehicleLogsAdapter(vehicleLogsList, listener(createLogBtnId,amountValueId,dateBtnId), listenerLongClick())
        recyclerView.adapter = vehicleLogsAdapter


            createLogBtnId.setOnClickListener { view ->

                    if (createLogBtnId.text == "UPDATE"){
                        db.updateMoneyCollected(applicationContext, Integer.parseInt(vehicleId.toString()), Integer.parseInt(amountValueId.text.toString()), dateBtnId.text.toString().replace(" ",""))
                        createLogBtnId.text = "ADD LOG"
                        markButtonDisable(dateBtnId,"SET DATE", true,R.color.white,R.color.purple_200)
                        attachAdapter()
//                        vehicleLogsAdapter.addLog(vehicleLogsList)

//                        startActivity(Intent(this,VehicalMain::class.java))


                    }else{

                        if ( amountValueId.text.toString().isEmpty() || dateBtnId.text == "SET DATE") {

                                 Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            } else {

                                var vehicleLog = VehicleLog(
                                    Integer.parseInt(amountValueId.text.toString()),
                                    dateHolder.toString().replace(" ",""),
                                    Integer.parseInt(vehicleId.toString())
                                )
                                //for reloading the list
                                var vehicleLogInsert = VehicleLog(
                                    Integer.parseInt(vehicleId.toString()),
                                    Integer.parseInt(amountValueId.text.toString()),
                                    dateHolder.toString().replace(" ",""),
                                    Integer.parseInt(target.toString().replace(" ",""))
                                )
//                            inserting into the database
                                db.insertLogs(applicationContext, vehicleLog)
                                amountValueId.setText("")
                                dateBtnId.setText("SET DATE")
//                                attachAdapter()
                                vehicleLogsAdapter.addLog(vehicleLogInsert)
                        }
                    }
            }
    }

    fun markButtonDisable(button: Button, date:String, check:Boolean, color:Int, bgColor:Int){
        button?.isEnabled=check
        button?.setText(date)
        button?.setTextColor(ContextCompat.getColor(applicationContext,color))
        button?.setBackgroundColor(ContextCompat.getColor(applicationContext,bgColor))
    }

    private fun listener(createLogBtnId:Button, amountValueId:TextInputEditText, dateBtnId:Button):VehicleLogsAdapter.OnItemClickListener{
        return object : VehicleLogsAdapter.OnItemClickListener {
            override fun onItemClick(vehicleLog: VehicleLog) {
                createLogBtnId.text = "UPDATE"
                amountValueId.setText(vehicleLog.money.toString().replace(" ",""))
                markButtonDisable(dateBtnId,vehicleLog.dateLogged.toString(), false,R.color.white,R.color.greyish)
            }
        }
    }

    private fun listenerLongClick():VehicleLogsAdapter.OnItemLongClickLister{
        return object : VehicleLogsAdapter.OnItemLongClickLister {
            override fun onItemLongClick(vehicleLog: VehicleLog): Boolean {
                var res_logs = db.deleteVehicleLog(vehicleLog.id)
                if (res_logs==1){

                Toast.makeText(applicationContext,"Deleted successfully ",Toast.LENGTH_SHORT).show()

                }else{

                    Toast.makeText(applicationContext,"Failed to delete log on ${vehicleLog.dateLogged}",Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
    }

    fun df(bj:VehicleLog){
        var builder = AlertDialog.Builder(applicationContext)
        builder.setTitle("Delete log made on ${bj.dateLogged}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){
                dialogInterface,which->
            run {
//                var res_logs = db.deleteVehicleLog(bj.id)
//                if (res_logs==1){
//
                    Toast.makeText(applicationContext,"Deleted successfully ",Toast.LENGTH_SHORT).show()
//
//                }else{
//
//                    Toast.makeText(applicationContext,"Failed to delete log on ${bj.dateLogged}",Toast.LENGTH_SHORT).show()
//                }
            }
        }
        builder.setNegativeButton("No"){
                dialogInterface,which->
            run {
                Toast.makeText(applicationContext,"Delete request Cancelled ",Toast.LENGTH_SHORT).show()
            }
        }
        var alert:AlertDialog=builder.create()
        alert.setCancelable(false)
        alert.show()
    }
    fun attachAdapter():Cursor{
       var data = db.getAllVehicleLogs(vehicleId)
        return data
    }
}