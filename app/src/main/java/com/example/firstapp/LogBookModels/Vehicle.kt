package com.example.firstapp.LogBookModels

open class Vehicle {
    var plate : String = ""
    var capacity : Int =0
    var route : String =""
    var target : Int= 0
    var vehicleId:Int=0
    constructor(){

    }
    constructor(plate:String, route:String,capacity:Int,target: Int){
        this.plate= plate
        this.route = route
        this.capacity = capacity
        this.target = target
    }

    constructor(vehicleId:Int,plate:String, route:String,capacity:Int,target: Int){
        this.vehicleId=vehicleId
        this.plate= plate
        this.route = route
        this.capacity = capacity
        this.target = target
    }
}