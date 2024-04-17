package com.example.pace.domain

import android.os.Parcel
import android.os.Parcelable

data class Workout (
    val workoutName: String? = null,
    val gymName: String? = null,
    val exercises: List<Exercise>? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Exercise)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(workoutName)
        parcel.writeString(gymName)
        parcel.writeTypedList(exercises)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Workout> {
        override fun createFromParcel(parcel: Parcel): Workout {
            return Workout(parcel)
        }

        override fun newArray(size: Int): Array<Workout?> {
            return arrayOfNulls(size)
        }
    }
}