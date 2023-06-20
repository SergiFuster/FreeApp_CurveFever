package com.example.freeapp_curvefever.Menu

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.freeapp_curvefever.Game.MainActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

class MenuModel {

    var playerColor : Int = 0
    var playerShipIndex : Int = 1
    var nRounds : Int = 0
    var nPlayers : Int = 0
    lateinit var gameInfo : GameInfo


    fun changePlayerShipIndex(i: Int) {
        playerShipIndex += i
        if (playerShipIndex <= 0) playerShipIndex = 6
        else if(playerShipIndex > 6) playerShipIndex = 1
    }

    fun updatePlayerColor(selectedColor: Int) {
        playerColor = selectedColor
    }

    fun createGameInfo(speedPowerUp: Boolean, sizeUpPowerUp: Boolean, sizeDownPowerUp: Boolean, jumpPowerUp: Boolean) {
        val powerUpString : MutableList<String> = arrayListOf()
        if(jumpPowerUp) powerUpString.add("JUMP")
        if(speedPowerUp) {
            powerUpString.add("SPEED_UP")
            powerUpString.add("SPEED_DOWN")
        }
        if(sizeDownPowerUp) powerUpString.add("SIZE_DOWN")
        if(sizeUpPowerUp) powerUpString.add("SIZE_UP")

        gameInfo = GameInfo(playerColor, playerShipIndex, nPlayers, nRounds, powerUpString)
    }

    fun saveState(outState : Bundle) {
        outState.putInt(PLAYER_COLOR, playerColor)
        outState.putInt(PLAYERS_NUMBER, nPlayers)
        outState.putInt(ROUNDS_NUMBER, nRounds)
        outState.putInt(PLAYER_SHIP, playerShipIndex)
    }

    fun setState(savedInstanceState: Bundle) {
        playerColor = savedInstanceState.getInt(PLAYER_COLOR)
        nPlayers = savedInstanceState.getInt(PLAYERS_NUMBER)
        nRounds = savedInstanceState.getInt(ROUNDS_NUMBER)
        playerShipIndex = savedInstanceState.getInt(PLAYER_SHIP)
    }

    fun checkInputs(players: String, rounds: String, validInput: () -> Unit, invalidInputListener : MyListener<String>) {
        if(players.isEmpty()) {
            invalidInputListener("Players number is empty")
            return
        }
        if (rounds.isEmpty()){
            invalidInputListener("Rounds number is empty")
            return
        }
        if(players.toInt() < 1 || players.toInt() > 6){
            invalidInputListener("Players number invalid, must be in range [1,6]")
            return
        }
        if(rounds.toInt() < 1 || rounds.toInt() > 6){
            invalidInputListener("Rounds number invalid, must be in range [1,6]")
            return
        }
        nPlayers = players.toInt()
        nRounds = rounds.toInt()
        validInput()
    }

    companion object{
        const val PLAYER_COLOR = "PLAYER_COLOR"
        const val PLAYERS_NUMBER = "PLAYERS_NUMBER"
        const val ROUNDS_NUMBER = "ROUNDS_NUMBER"
        const val PLAYER_SHIP = "PLAYER_SHIP"
    }
}