package com.example.pace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pace.R

class WorkoutAdapter(private val workoutNames: List<String>) :
    RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutNameTextView: TextView = itemView.findViewById(R.id.workoutNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.workoutNameTextView.text = workoutNames[position]
    }

    override fun getItemCount(): Int {
        return workoutNames.size
    }
}
