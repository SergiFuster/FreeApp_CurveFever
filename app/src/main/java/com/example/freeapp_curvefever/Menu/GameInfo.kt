package com.example.freeapp_curvefever.Menu

import android.os.Parcel
import android.os.Parcelable

data class GameInfo (
    val playerColor : Int,
    val shipIndex : Int,
    val nPlayers : Int,
    val nRounds : Int,
    val powerUps : List<String>,
    val soundActive : Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        mutableListOf<String>().apply {
            parcel.readStringList(this)
        },
        parcel.readBoolean()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(playerColor)
        parcel.writeInt(shipIndex)
        parcel.writeInt(nPlayers)
        parcel.writeInt(nRounds)
        parcel.writeStringList(powerUps)
        parcel.writeBoolean(soundActive)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameInfo> {
        override fun createFromParcel(parcel: Parcel): GameInfo {
            return GameInfo(parcel)
        }

        override fun newArray(size: Int): Array<GameInfo?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Color: $playerColor\n" +
                "Ship: $shipIndex\n" +
                "Rounds: $nRounds\n" +
                "Players: $nPlayers\n" +
                "PowerUps: $powerUps"
    }
}