package com.example.pace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pace.R
import com.example.pace.domain.Workout

class WorkoutAdapter(private val workouts: List<Workout>,
                     private val deleteCallback: (Workout) -> Unit) :
    RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(workout: Workout)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val workoutNameTextView: TextView = itemView.findViewById(R.id.workoutNameTextView)
        val workoutGymNameTextView: TextView = itemView.findViewById(R.id.workoutGymNameTextView)

        init {
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val workout = workouts[position]
                    deleteCallback(workout)
                    true
                } else {
                    false
                }
            }

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(workouts[position])
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workout = workouts[position]
        holder.workoutNameTextView.text = workout.workoutName
        holder.workoutGymNameTextView.text = workout.gymName
    }

    override fun getItemCount(): Int {
        return workouts.size
    }
}
