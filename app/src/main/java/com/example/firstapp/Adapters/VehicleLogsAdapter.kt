package com.example.firstapp.Adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.LogBookModels.Vehicle
import com.example.firstapp.LogBookModels.VehicleLog
import com.example.firstapp.R

class VehicleLogsAdapter(var vehicleLog:ArrayList<VehicleLog>, private var onItemClickListener: OnItemClickListener,private  var onItemLongClickLister: VehicleLogsAdapter.OnItemLongClickLister): RecyclerView.Adapter<VehicleLogsAdapter.VehicleLogsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleLogsViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.vehicle_daily_logs, parent, false)

        return VehicleLogsViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleLogsViewHolder, position: Int) {
        var vehicleItem = vehicleLog[position]
        holder.dailyAmount.text = "Mk"+vehicleItem.money.toString()
        holder.dateCollected.text = vehicleItem.dateLogged.toString()
        holder.balance.text = "Mk"+(vehicleItem.money-vehicleItem.target).toString()
        holder?.bindView(vehicleItem, onItemClickListener,onItemLongClickLister)

    }
    interface OnItemClickListener {
        fun onItemClick(vehicleLog: VehicleLog)
    }
    interface OnItemLongClickLister{
        fun onItemLongClick(vehicleLog: VehicleLog):Boolean
    }
    override fun getItemCount(): Int {
        return vehicleLog.size
    }

    fun addLog(log:VehicleLog){
        vehicleLog.add(0,log)
        notifyDataSetChanged()
    }

    class VehicleLogsViewHolder(itemVie:View):RecyclerView.ViewHolder(itemVie){
        var dailyAmount = itemView.findViewById<TextView>(R.id.daily_amount)
        var dateCollected = itemView.findViewById<TextView>(R.id.date_entered)
        var balance = itemView.findViewById<TextView>(R.id.balance)

        fun bindView(vehicleLog: VehicleLog, onItemClickListener: VehicleLogsAdapter.OnItemClickListener,onItemLongClickLister: VehicleLogsAdapter.OnItemLongClickLister) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(vehicleLog)
            }
            itemView.setOnLongClickListener {
                onItemLongClickLister.onItemLongClick(vehicleLog)
            }
        }
    }

}

