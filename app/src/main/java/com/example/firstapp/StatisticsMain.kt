package com.example.firstapp

import android.annotation.SuppressLint
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.firstapp.Database.DatabaseHelper
import com.example.firstapp.LogBookModels.Vehicle
import com.example.firstapp.LogBookModels.VehicleLog
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatisticsMain : AppCompatActivity() {
    var db = DatabaseHelper(this)
    var vehicles = ArrayList<String>()
    var plateHolder:String =""
    var month_holder_int:Int=0
    var vehicleTagert=ArrayList<Int>()
    var vehicleIDList = ArrayList<Int>()
    var targetMoney:Int=0
    var vehicleId:Int=0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_main)
        var updateTable = findViewById<Button>(R.id.update_table)
        var vehicle_spinner = findViewById<Spinner>(R.id.vehicle_spinner)
        var month_holder = findViewById<TextView>(R.id.month_holder)
        var year_holder = findViewById<TextView>(R.id.year_holder)
        var month_year_btn = findViewById<Button>(R.id.month_year)
        var totalDays = findViewById<TextView>(R.id.total_days)
        var total_collection = findViewById<TextView>(R.id.total_collection)
        var total_target = findViewById<TextView>(R.id.total_target)
        var totalBalance = findViewById<TextView>(R.id.total_balance)
        var average_collection = findViewById<TextView>(R.id.average_collection)
        var maximum = findViewById<TextView>(R.id.maximum)
        var minimum = findViewById<TextView>(R.id.minimum)
        var data = db.getAllVehicles()
        vehicleSpinner(data, vehicle_spinner, vehicles)
        yearMonthFun(month_year_btn, month_holder, year_holder)

        var newList:kotlin.collections.ArrayList<Int> = ArrayList()

        updateTable.setOnClickListener{
            if (year_holder.text.isEmpty() && month_holder.text.isEmpty()){
                Toast.makeText(applicationContext,"Set period first!",Toast.LENGTH_SHORT).show()
            }else{
                newList = getAllLogDataMonthly(vehicleId, month_holder_int,Integer.parseInt(year_holder.text.toString()))
                totalDays.text = newList.size.toString()
                var total:Int= totalCollection(newList)
                total_collection.text = "Mk"+total.toString()
                var totalTagt:Int = totalTarget(targetMoney,newList.size)
                total_target.text = "Mk"+totalTagt.toString()
                totalBalance.text = "Mk"+(total-totalTagt).toString()
                average_collection.text = "Mk"+average(total,newList.size).toString()
                minimum.text = minimum(newList).toString()
                maximum.text = maximum(newList).toString()
            }

        }
    }

    fun vehicleSpinner(data:Cursor, vehicle_spinner: Spinner, vehicle:ArrayList<String>){

        while (data.moveToNext()){
            var hh = Vehicle(data.getInt(0),data.getString(1),data.getString(2),data.getInt(3),data.getInt(4))
            vehicleTagert.add(hh.target)
            vehicleIDList.add(hh.vehicleId)
            vehicle.add(hh.plate)
        }
        var arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,vehicle)
        vehicle_spinner.adapter = arrayAdapter

        vehicle_spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                plateHolder = vehicles[position].toString()
                targetMoney = vehicleTagert[position]
                vehicleId = vehicleIDList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    fun yearMonthFun(month_year:Button,month:TextView, year:TextView){

        month_year.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("MM-yyyy")
                val date = dateFormatter.format(Date(it))
//                dateHolder = date
                var dateList: List<String> = date.split("-")
                month_holder_int = Integer.parseInt(dateList[0])
                month.setText(getMonthString(Integer.parseInt(dateList[0])-1))
                year.setText(dateList[1].toString())

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
    }

    private fun getMonthString(month: Int): String {
        val result = when (month) {
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "Jun"
            6 -> "Jul"
            7 -> "Aug"
            8 -> "Sept"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> {
                "Jan"
            }
        }
        return result.uppercase()
    }

    fun getAllLogDataMonthly(id:Int, m:Int,y:Int) :kotlin.collections.ArrayList<Int>{
        var vehicleLogsMonthly = db.getAllVehicleLogsPerYearMonth(id,m,y)
        var holderl:kotlin.collections.ArrayList<Int> = ArrayList()
        if (vehicleLogsMonthly.count==0 || vehicleLogsMonthly==null){
            Toast.makeText(applicationContext,"No records found for this period!",Toast.LENGTH_LONG).show()

        }else{
            while (vehicleLogsMonthly.moveToNext()){
                var hh = VehicleLog(vehicleLogsMonthly.getInt(0),vehicleLogsMonthly.getInt(1),vehicleLogsMonthly.getString(2),vehicleLogsMonthly.getInt(0),vehicleLogsMonthly.getInt(3),vehicleLogsMonthly.getInt(4),vehicleLogsMonthly.getInt(5))
                holderl.add(hh.money.toInt())
            }
        }

        return holderl
    }

    fun totalCollection(money:kotlin.collections.ArrayList<Int>):Int{
        var sum:Int = 0
        for (x in money){
            sum+=x
        }
        return sum
    }

    fun totalTarget(tgt:Int, colle:Int):Int{
        var total:Int=0
        total = tgt*colle
        return total
    }

    fun average(total:Int,count:Int):Int{
        if (count==0){
            return 0

        }else{
            return total/count
        }
    }

    fun minimum(moneyCollection:kotlin.collections.ArrayList<Int>):Int{
        var min:Int = moneyCollection.minOrNull()?:0
        return min
    }

    fun maximum(moneyCollection:kotlin.collections.ArrayList<Int>):Int{
        var max:Int = moneyCollection.maxOrNull()?:0
        return max
    }
}