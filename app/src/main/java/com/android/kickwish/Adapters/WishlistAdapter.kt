package com.android.kickwish.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Models.Wish
import com.android.kickwish.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class WishlistAdapter(private val wishes: MutableList<Wish>) :
    RecyclerView.Adapter<WishlistAdapter.WishViewHolder>() {

    private lateinit var onItemClickCall: OnItemClickCallBack

    interface OnItemClickCallBack {
        fun deleteWish(wish: Wish)
        fun exploreWish(wish: Wish)
    }

    fun setItemOnClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCall = onItemClickCallBack
    }

    class WishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _ivWishItem = itemView.findViewById<ImageView>(R.id.ivWishItem)
        var _tvSneakersName = itemView.findViewById<TextView>(R.id.tvSneakersName)
        var _tvSneakersPrice = itemView.findViewById<TextView>(R.id.tvSneakersPrice)
        var _btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
        var _btnExplore = itemView.findViewById<ImageButton>(R.id.btnExplore)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistAdapter.WishViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.wish_item, parent, false
        )
        return WishViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistAdapter.WishViewHolder, position: Int) {
        var wish = wishes[position]

        Picasso.get()
            .load(wish.sneaker.imageUrl)
            .into(holder._ivWishItem)
        holder._tvSneakersName.setText(wish.sneaker.name)
        holder._tvSneakersPrice.setText(
            NumberFormat.getCurrencyInstance(
                Locale("id", "ID")
            ).format(wish.sneaker.price)
        )

        holder._btnDelete.setOnClickListener {
            onItemClickCall.deleteWish(wish)
        }

        holder._btnExplore.setOnClickListener {
            onItemClickCall.exploreWish(wish)
        }
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