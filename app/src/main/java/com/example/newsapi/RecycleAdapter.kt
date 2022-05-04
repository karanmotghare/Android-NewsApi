package com.example.newsapi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val sectionName: TextView =itemView.findViewById(R.id.txt_view1)
    val webTitle: TextView = itemView.findViewById(R.id.txt_view2)
}

class RecycleAdapter(private val context: Context, private val items: ArrayList<Data>): RecyclerView.Adapter<viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_res, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val secName = items[position].sectionName
        val webTil = items[position].webTitle
        holder.sectionName.text = secName
        holder.webTitle.text = webTil
        holder.webTitle.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(items[position].webUrl))
            context.startActivity(browserIntent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}