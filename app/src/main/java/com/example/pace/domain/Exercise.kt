package com.example.pace.domain

import android.os.Parcel
import android.os.Parcelable

data class Exercise (
    val name: String? = null,
    val reps: Int = 0,
    val kg: Double = 0.0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(reps)
        parcel.writeDouble(kg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exercise> {
        override fun createFromParcel(parcel: Parcel): Exercise {
            return Exercise(parcel)
        }

        override fun newArray(size: Int): Array<Exercise?> {
            return arrayOfNulls(size)
        }
    }
}