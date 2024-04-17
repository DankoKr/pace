package com.example.pace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pace.R
import com.example.pace.domain.Exercise

class ExerciseAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    // ViewHolder for each item in the RecyclerView
    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseName)
        val kgTextView: TextView = itemView.findViewById(R.id.kgField)
        val repsTextView: TextView = itemView.findViewById(R.id.repsField)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_item, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseNameTextView.text = exercise.name
        holder.kgTextView.text = exercise.kg.toString()
        holder.repsTextView.text = exercise.reps.toString()
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}