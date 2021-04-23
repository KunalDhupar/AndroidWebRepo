package com.cg.androidweb

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView



/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class PlaceRecyclerViewAdapter(
    private val values: List<Place>
) : RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_place_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nameT.text = item.placeName
        holder.cityT.text = item.city
        holder.ccodeT.text=item.CCode
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameT: TextView = view.findViewById(R.id.nameT)
        val cityT: TextView = view.findViewById(R.id.cityT)
        val ccodeT:TextView=view.findViewById(R.id.codeT)

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }
}