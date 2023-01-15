package com.example.firstapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.LogBookModels.Vehicle
import com.example.firstapp.R

class VehicleAdapter(val vehicleList:ArrayList<Vehicle>, private  var onItemClickListener: OnItemClickListener,private  var onItemLongClickLister:OnItemLongClickLister) :
    RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.card_vehicle_layout, parent, false)

        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
       var vehicleItem = vehicleList[position]
        holder.vehicle_plate.text = vehicleItem.plate
        holder.vehicle_route.text = vehicleItem.route
        holder.vehicle_target.text = "Mk"+vehicleItem.target.toString()
        holder?.bindView(vehicleItem, onItemClickListener,onItemLongClickLister)
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    interface OnItemClickListener {
        fun onItemClick(vehicle: Vehicle)
    }
    interface OnItemLongClickLister{
        fun onItemLongClick(vehicle: Vehicle):Boolean
    }
    class VehicleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var vehicle_route = itemView.findViewById<TextView>(R.id.vehicle_route)
        var vehicle_target = itemView.findViewById<TextView>(R.id.vehicle_target)
        var vehicle_plate = itemView.findViewById<TextView>(R.id.vehicle_plate)

        fun bindView(vehicle: Vehicle, onItemClickListener: OnItemClickListener,onItemLongClickLister: OnItemLongClickLister) {
            itemView.setOnClickListener {
                onItemClickListener.onItemClick(vehicle)
            }
            itemView.setOnLongClickListener {
                onItemLongClickLister.onItemLongClick(vehicle)
            }
        }
    }

}