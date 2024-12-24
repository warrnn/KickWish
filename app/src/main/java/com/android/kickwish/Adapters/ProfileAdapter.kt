package com.android.kickwish.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Models.Wish
import com.android.kickwish.R
import java.text.NumberFormat
import java.util.Locale

class ProfileAdapter(private val wishes: MutableList<Wish>):
RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){

    private lateinit var onItemClickCall: OnItemClickCallBack

    interface OnItemClickCallBack {

    }


    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvShoeName = itemView.findViewById<TextView>(R.id.TVShoeName)
        var _tvShoePrice = itemView.findViewById<TextView>(R.id.TVShoePrice)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileAdapter.ProfileViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.profile_item, parent, false
        )

        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileAdapter.ProfileViewHolder, position: Int) {
        var this_wish = wishes[position]

        holder._tvShoeName.setText(this_wish.sneaker.name)
        holder._tvShoePrice.setText(
            NumberFormat.getCurrencyInstance(
                Locale("id", "ID")
            ).format(this_wish.sneaker.price)
        )
    }

    override fun getItemCount(): Int {
        return wishes.size
    }

    fun loadData(getWishes: List<Wish>) {
        this.wishes.clear()
        this.wishes.addAll(getWishes)
        notifyDataSetChanged()
    }


}