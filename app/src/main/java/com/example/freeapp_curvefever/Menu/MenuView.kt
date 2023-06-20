package com.example.freeapp_curvefever.Menu

import android.content.Context

interface MenuView {

    fun changePlayerShipImage(index : Int)
    fun invalidInput(message: String)
    fun validInput()
    fun showPlayerColorPickerDialog()
    fun startGameActivity(gameInfo: GameInfo)
}