package com.example.freeapp_curvefever.Menu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.freeapp_curvefever.Game.MainActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

class MenuPresenter(val view : MenuView, val model : MenuModel) {

    fun playButtonPressed(
        speedPowerUp : Boolean,
        sizeUpPowerUp : Boolean,
        sizeDownPowerUp : Boolean,
        jumpPowerUp : Boolean
    ) {
        model.createGameInfo(
            speedPowerUp,
            sizeUpPowerUp,
            sizeDownPowerUp,
            jumpPowerUp
        )
        view.startGameActivity(model.gameInfo)
    }

    fun changePlayerShipIndex(i: Int) {
        model.changePlayerShipIndex(i)
        view.changePlayerShipImage(model.playerShipIndex)
    }

    fun playerColorPicked(selectedColor: Int) {
        model.updatePlayerColor(selectedColor)
    }

    fun colorPickerPressed() {
        view.showPlayerColorPickerDialog()
    }

    fun saveState(outState : Bundle) {
        model.saveState(outState)
    }

    fun setState(savedInstanceState: Bundle) {
        model.setState(savedInstanceState)
        view.changePlayerShipImage(model.playerShipIndex)
    }

    fun checkInputs(players: String, rounds: String) {
        model.checkInputs(
            players,
            rounds,
            {view.validInput()},
            {
                message -> view.invalidInput(message)
            }
        )
    }


}