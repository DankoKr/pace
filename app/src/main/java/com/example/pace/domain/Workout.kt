package com.example.pace.domain

import android.os.Parcel
import android.os.Parcelable

data class Workout (
    var id: String? = null, // This field is used locally and not stored in Firestore
    var workoutName: String? = null,
    var gymName: String? = null,
    var exercises: List<Exercise>? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Exercise)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
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