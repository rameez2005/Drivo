package com.example.drivo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.R
import com.example.drivo.activities.MainActivity
import com.example.drivo.fragments.DriverDetailFragment
import com.example.drivo.models.Driver
import android.os.Bundle

class DriverAdapter(
    private val context: Context,
    private var driverList: List<Driver>
) : RecyclerView.Adapter<DriverAdapter.DriverViewHolder>() {

    class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_driver_name)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_driver_phone)
        val tvVehicle: TextView = itemView.findViewById(R.id.chip_vehicle)
        val tvAvailability: TextView = itemView.findViewById(R.id.chip_available)
        val tvRating: TextView = itemView.findViewById(R.id.chip_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_driver_card, parent, false)
        return DriverViewHolder(view)
    }

    override fun onBindViewHolder(holder: DriverViewHolder, position: Int) {
        val driver = driverList[position]

        holder.tvName.text = driver.fullName
        holder.tvPhone.text = driver.phone
        holder.tvVehicle.text = driver.assignedVehicle
        holder.tvAvailability.text = driver.availabilityStatus
        holder.tvRating.text = "★ ${driver.performanceRating}"


        // Item click — pass to detail
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("DRIVER_DATA", driver)
            val detailFragment = DriverDetailFragment()
            detailFragment.arguments = bundle
            (context as MainActivity).loadFragment(detailFragment)
        }
    }

    override fun getItemCount() = driverList.size

    // Called by search/filter (F5)
    fun updateList(newList: List<Driver>) {
        driverList = newList
        notifyDataSetChanged()
    }
}

