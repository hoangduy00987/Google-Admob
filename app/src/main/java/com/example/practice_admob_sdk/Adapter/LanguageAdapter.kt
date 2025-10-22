package com.example.practice_admob_sdk.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_admob_sdk.Model.LanguageItem
import com.example.practice_admob_sdk.R

class LanguageAdapter(

    private val items: MutableList<LanguageItem>,
    private val onIntemclick: (LanguageItem) -> Unit
): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>(){
    private var selectedPosition = RecyclerView.NO_POSITION
    inner class LanguageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imgFlag: ImageView = view.findViewById(R.id.imgFlag)
        val tvName: TextView = view.findViewById(R.id.tvLanguageName)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)

    }
    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {

        val item = items[position]
        holder.tvName.text = item.name
        holder.imgFlag.setImageResource(item.flagResId)
        holder.itemView.isSelected = position == selectedPosition


        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onIntemclick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
