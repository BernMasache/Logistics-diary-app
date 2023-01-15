package com.example.firstapp.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.firstapp.LogBookModels.Vehicle
import com.example.firstapp.LogBookModels.VehicleLog
import com.example.firstapp.VehicalMain

const val DATABASE_NAME = "LogBook.db"
const val TABLE_NAME = "vehicles"

const val COLUMN_NAME_PLATE_NUMBER= "plate_number"
const val COLUMN_NAME_ROUTE = "vehicle_route"
const val COLUMN_NAME_TARGET= "target"
const val COLUMN_NAME_CAPACITY= "capacity"
const val COLUMN_NAME_ID="vehicle_id"

//logs
const val TABLE_NAME_LOGS = "log_book"
const val COLUMN_NAME_LOG_ID="log_id"
const val COLUMN_NAME_CASH= "money"
const val COLUMN_NAME_DATE= "date"
const val COLUMN_NAME_VEHICLE_ID="vehicle_id"
const val COLUMN_NAME_VEHICLE_LOG_YEAR="year"
const val COLUMN_NAME_VEHICLE_LOG_MONTH="month"

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        var create = "CREATE TABLE "+ TABLE_NAME+" ("+COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME_PLATE_NUMBER+" VARCHAR(100), "+COLUMN_NAME_ROUTE+" VARCHAR(20), "+
                COLUMN_NAME_CAPACITY +" INTEGER, "+COLUMN_NAME_TARGET+" INTEGER)"
        db.execSQL(create)

        var createLogs = "CREATE TABLE "+ TABLE_NAME_LOGS+" ("+COLUMN_NAME_LOG_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME_CASH+" INTEGER, "+COLUMN_NAME_DATE+" VARCHAR(100), "+
                COLUMN_NAME_VEHICLE_ID +" INTEGER, "+ COLUMN_NAME_VEHICLE_LOG_MONTH+" INTEGER, "+COLUMN_NAME_VEHICLE_LOG_YEAR+" INTEGER)"
        db.execSQL(createLogs)

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    fun insertVehicle(context: Context,vehicle: Vehicle){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME_PLATE_NUMBER, vehicle.plate)
        values.put(COLUMN_NAME_ROUTE, vehicle.route)
        values.put(COLUMN_NAME_CAPACITY, vehicle.capacity)
        values.put(COLUMN_NAME_TARGET, vehicle.target)

       val result= db?.insert(TABLE_NAME,null,values)
//
        if (result== -1.toLong()){
            Toast.makeText(context,"Failed to create vehicle",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Created vehicle successfully",Toast.LENGTH_SHORT).show()
            var intent = Intent(context, VehicalMain::class.java)
            context.startActivity(intent)
        }

    }
    fun insertLogs(context: Context,vehicleLog: VehicleLog){
        var year_mont:List<String> = vehicleLog.dateLogged.split("-")
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME_VEHICLE_ID, vehicleLog.vehicleId)
        values.put(COLUMN_NAME_CASH, vehicleLog.money)
        values.put(COLUMN_NAME_DATE, vehicleLog.dateLogged)
        values.put(COLUMN_NAME_VEHICLE_LOG_MONTH, Integer.parseInt(year_mont[1]))
        values.put(COLUMN_NAME_VEHICLE_LOG_YEAR, Integer.parseInt(year_mont[2]))

        val result= db?.insert(TABLE_NAME_LOGS,null,values)
//
        if (result== -1.toLong()){
            Toast.makeText(context,"Failed to create in the log book",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Created log successfully",Toast.LENGTH_SHORT).show()

        }

    }

    @SuppressLint("Range")
    fun getAllVehicles(): Cursor{
        var sqlRead = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sqlRead,null)
        return  cursor
    }

    fun getAllVehicleLogs(ID:Int): Cursor{
        var sqlRead = "SELECT * FROM $TABLE_NAME_LOGS WHERE $COLUMN_NAME_VEHICLE_ID LIKE '"+ID+"' ORDER BY $COLUMN_NAME_LOG_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sqlRead,null)
        return  cursor
    }

    fun getAllVehicleLogsById(ID:Int): Cursor{
        var sqlRead = "SELECT * FROM $TABLE_NAME_LOGS WHERE $COLUMN_NAME_VEHICLE_ID LIKE '"+ID+"' ORDER BY $COLUMN_NAME_LOG_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sqlRead,null)
        return  cursor
    }
    fun getAllVehicleLogsPerYearMonth(ID:Int, Month:Int,Year:Int): Cursor{
        var sqlRead = "SELECT * FROM $TABLE_NAME_LOGS WHERE $COLUMN_NAME_VEHICLE_ID LIKE '"+ID+"' AND $COLUMN_NAME_VEHICLE_LOG_MONTH LIKE '"+Month+"' AND $COLUMN_NAME_VEHICLE_LOG_YEAR LIKE '"+Year+"'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sqlRead,null)
        return  cursor
    }

    fun updateMoneyCollected(context: Context, id:Int, money:Int,date:String){
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(COLUMN_NAME_CASH, money )
        var res =  db.update(TABLE_NAME_LOGS, value, COLUMN_NAME_VEHICLE_ID+"='"+id+"' AND "+COLUMN_NAME_DATE+"='"+date+"'", null)
        if (res >0){
            Toast.makeText(context,"Updated amount for "+date.toString(),Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Failed to Update amount for "+date.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    fun updateVehicle(context: Context,id:Int, plate:String, target:Int,capacity:Int, route:String):Int{
        val db = this.writableDatabase
        val value = ContentValues()
        value.put(COLUMN_NAME_PLATE_NUMBER, plate )
        value.put(COLUMN_NAME_TARGET, target )
        value.put(COLUMN_NAME_CAPACITY, capacity )
        value.put(COLUMN_NAME_ROUTE, route )

        var res =  db.update(TABLE_NAME, value, COLUMN_NAME_ID+"='"+id+"'", null)

        if (res >0){
            var vehicleList = getAllVehicleLogsById(id)

            Toast.makeText(context,"Updated vehicle records successfully",Toast.LENGTH_SHORT).show()
            return 1

        }else{
            Toast.makeText(context,"Failed to Update the vehicle records",Toast.LENGTH_SHORT).show()
            return 0
        }

    }

    fun deleteVehicle(ID: Int):Int{
        var db = this.readableDatabase
        var del = db.delete(TABLE_NAME,COLUMN_NAME_ID+"='"+ID+"'",null)
        return del
    }
    fun deleteVehicleLogs(ID: Int):Int{
        var db = this.readableDatabase
        var del = db.delete(TABLE_NAME_LOGS,COLUMN_NAME_VEHICLE_ID+"='"+ID+"'",null)
        return del
    }
    fun deleteVehicleLog(ID: Int):Int{
        var db = this.readableDatabase
        var del = db.delete(TABLE_NAME_LOGS,COLUMN_NAME_LOG_ID+"='"+ID+"'",null)
        return del
    }
}
