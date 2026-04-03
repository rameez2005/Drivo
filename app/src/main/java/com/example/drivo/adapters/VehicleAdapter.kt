package com.example.drivo.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.R
import com.example.drivo.activities.MainActivity
import com.example.drivo.fragments.VehicleDetailFragment
import com.example.drivo.models.Vehicle
import android.os.Bundle

class VehicleAdapter(
    private val context: Context,
    private var vehicleList: List<Vehicle>
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRegNumber: TextView = itemView.findViewById(R.id.tv_reg_number)
        val tvMakeModel: TextView = itemView.findViewById(R.id.tv_make_model)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvDriver: TextView = itemView.findViewById(R.id.tv_driver)
        val tvRoute: TextView = itemView.findViewById(R.id.tv_route)
        val viewStatusDot: View = itemView.findViewById(R.id.view_status_dot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_vehicle_card, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val vehicle = vehicleList[position]

        holder.tvRegNumber.text = vehicle.registrationNumber
        holder.tvMakeModel.text = "${vehicle.make} ${vehicle.model} (${vehicle.year})"
        holder.tvStatus.text = vehicle.status
        holder.tvDriver.text = vehicle.assignedDriver
        holder.tvRoute.text = vehicle.assignedRoute

        // Status dot color
        val dotColor = when (vehicle.status) {
            "ACTIVE" -> R.color.colorStatusActive
            "MAINTENANCE" -> R.color.colorStatusMaintenance
            else -> R.color.colorStatusRetired
        }
        holder.viewStatusDot.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, dotColor))

        // Item click — pass to detail
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("VEHICLE_DATA", vehicle)
            val detailFragment = VehicleDetailFragment()
            detailFragment.arguments = bundle
            (context as MainActivity).loadFragment(detailFragment)
        }
    }

    override fun getItemCount() = vehicleList.size

    // Called by search/filter (F5)
    fun updateList(newList: List<Vehicle>) {
        vehicleList = newList
        notifyDataSetChanged()
    }
}

