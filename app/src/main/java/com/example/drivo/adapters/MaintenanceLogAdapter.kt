package com.example.drivo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drivo.R
import com.example.drivo.data.local.MaintenanceLog

class MaintenanceLogAdapter : RecyclerView.Adapter<MaintenanceLogAdapter.MaintenanceViewHolder>() {

    private var logs: List<MaintenanceLog> = emptyList()

    class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.tv_log_title)
        val metaText: TextView = itemView.findViewById(R.id.tv_log_meta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_maintenance_log, parent, false)
        return MaintenanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        val log = logs[position]
        holder.titleText.text = "${log.partName} - ${log.actionTaken}"
        holder.metaText.text = "${log.serviceDate} | PKR %.0f".format(log.cost)
    }

    override fun getItemCount(): Int = logs.size

    fun submitList(newLogs: List<MaintenanceLog>) {
        logs = newLogs
        notifyDataSetChanged()
    }
}

