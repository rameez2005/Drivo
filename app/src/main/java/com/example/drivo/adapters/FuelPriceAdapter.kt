package com.example.drivo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drivo.R
import com.example.drivo.data.remote.FuelPrice

class FuelPriceAdapter(
    private val onItemClick: (FuelPrice) -> Unit
) : RecyclerView.Adapter<FuelPriceAdapter.FuelPriceViewHolder>() {

    private var items: List<FuelPrice> = emptyList()

    class FuelPriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_article_image)
        val titleText: TextView = itemView.findViewById(R.id.tv_article_title)
        val sourceText: TextView = itemView.findViewById(R.id.tv_article_source)
        val dateText: TextView = itemView.findViewById(R.id.tv_updated_at)
        val descriptionText: TextView = itemView.findViewById(R.id.tv_article_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelPriceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fuel_price, parent, false)
        return FuelPriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: FuelPriceViewHolder, position: Int) {
        val item = items[position]
        holder.titleText.text = item.title
        holder.sourceText.text = item.sourceName
        holder.dateText.text = item.publishedAt
        holder.descriptionText.text = item.description.ifBlank { item.content }

        Glide.with(holder.itemView)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_reports)
            .error(R.drawable.ic_reports)
            .centerCrop()
            .into(holder.imageView)

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<FuelPrice>) {
        items = newItems
        notifyDataSetChanged()
    }
}

