package com.youtube.firebaserealtime.presentation.cats

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.youtube.firebaserealtime.R
import com.youtube.firebaserealtime.extensions.load
import com.youtube.firebaserealtime.models.Cat

class CatsAdapter(
    private val context: Context,
    private val cats: List<Cat>
): RecyclerView.Adapter<CatsAdapter.CatViewHolder>()  {
    class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCat: ImageView = itemView.findViewById(R.id.ivCat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cell_cat, parent, false)
        return CatViewHolder(view)
    }
    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        with(cats[position]) {
            holder.ivCat.load(url, fitCenterCrop = true)
        }
    }
    override fun getItemCount() = cats.size
}
