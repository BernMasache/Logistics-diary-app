package com.example.firstapp.LogBookModels

class VehicleLog {
    var vehicleId : Int = 0
    var money : Int =0
    var dateLogged : String =""
    var target:Int=0
    var id:Int = 0
    var month:Int=0
    var year:Int=0
    constructor(money:Int,dateLogged:String,vehicleId:Int){
        this.vehicleId= vehicleId
        this.dateLogged = dateLogged
        this.money = money
    }

    constructor(id:Int,money:Int,dateLogged:String,vehicleId:Int, target:Int){
        this.vehicleId= vehicleId
        this.dateLogged = dateLogged
        this.money = money
        this.target = target
        this.id = id
    }
    constructor(id:Int,money:Int,dateLogged:String, target:Int){
        this.vehicleId= vehicleId
        this.dateLogged = dateLogged
        this.money = money
        this.target = target
        this.id = id
    }
    constructor(id:Int,money:Int,dateLogged:String,vehicleId:Int, target:Int,month:Int,year:Int){
        this.vehicleId= vehicleId
        this.dateLogged = dateLogged
        this.money = money
        this.target = target
        this.id = id
        this.month=month
        this.year = year
    }
}