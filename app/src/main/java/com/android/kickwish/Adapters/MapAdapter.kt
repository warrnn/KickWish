package com.android.kickwish.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Models.Store
import com.android.kickwish.R

class MapAdapter(private var maps: MutableList<Store>) : RecyclerView.Adapter<MapAdapter.MapViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null

    interface OnItemClickCallBack {
        fun onItemClicked(store: Store)
    }

    fun setOnItemClickCallBack(callback: OnItemClickCallBack) {
        this.onItemClickCallBack = callback
    }

    fun setFilteredList(arrMaps: MutableList<Store>){
        this.maps = arrMaps
        notifyDataSetChanged()
    }

    class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvStoreName = itemView.findViewById<TextView>(R.id.TVShopName)
        var _tvStoreDesc = itemView.findViewById<TextView>(R.id.TVDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.map_item, parent, false
        )
        return MapViewHolder(view)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val this_store = maps[position]
        holder._tvStoreName.text = this_store.name
        holder._tvStoreDesc.text = this_store.desc

        holder.itemView.setOnClickListener {
            onItemClickCallBack?.onItemClicked(this_store)
        }
    }

    override fun getItemCount(): Int {
        return maps.size
    }

    fun loadData(getStores: List<Store>) {
        this.maps.clear()
        this.maps.addAll(getStores)
        notifyDataSetChanged()
    }
}
