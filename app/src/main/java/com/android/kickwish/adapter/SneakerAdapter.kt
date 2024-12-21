package com.android.kickwish.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.R
import com.android.kickwish.DetailActivity
import com.android.kickwish.database.Sneaker
import java.text.NumberFormat
import java.util.Locale

class SneakerAdapter(private val sneakers: List<Sneaker>) :
    RecyclerView.Adapter<SneakerAdapter.SneakerViewHolder>() {

    class SneakerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.sneakerImage)
        val nameText: TextView = view.findViewById(R.id.sneakerName)
        val priceText: TextView = view.findViewById(R.id.sneakerPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalog, parent, false)
        return SneakerViewHolder(view)
    }

    override fun onBindViewHolder(holder: SneakerViewHolder, position: Int) {
        val sneaker = sneakers[position]
        holder.imageView.setImageResource(sneaker.imageUrl)
        holder.nameText.text = sneaker.name

        // Format price in IDR
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        holder.priceText.text = formatter.format(sneaker.price)

        // Set click listener to open detail activity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = DetailActivity.createIntent(
                context,
                sneaker.id,
                sneaker.name,
                sneaker.price,
                sneaker.imageUrl
            )
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = sneakers.size
}