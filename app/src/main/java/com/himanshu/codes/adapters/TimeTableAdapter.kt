package com.himanshu.codes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.codes.R
import com.himanshu.codes.time.Time

class TimeTableAdapter(private val classList: ArrayList<Time>):
    RecyclerView.Adapter<TimeTableAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val classTime: TextView = itemView.findViewById(R.id.Subject_card_class_time)
        val classTitle: TextView = itemView.findViewById(R.id.Subject_card_subject_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.timetable_card,parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.classTime.text = classList[position].getClassTime()
        holder.classTitle.text = classList[position].getClassName()
    }

    override fun getItemCount(): Int = classList.size
}