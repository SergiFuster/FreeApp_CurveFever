package com.example.freeapp_curvefever.Menu

import android.os.Bundle

class MenuPresenter(val view : MenuView, val model : MenuModel) {

    fun playButtonPressed(
        speedPowerUp : Boolean,
        sizeUpPowerUp : Boolean,
        sizeDownPowerUp : Boolean,
        jumpPowerUp : Boolean,
        soundActive : Boolean
    ) {
        model.createGameInfo(
            speedPowerUp,
            sizeUpPowerUp,
            sizeDownPowerUp,
            jumpPowerUp,
            soundActive
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