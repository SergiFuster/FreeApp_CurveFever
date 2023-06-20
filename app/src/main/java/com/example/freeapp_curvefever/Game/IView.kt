package com.example.freeapp_curvefever.Game

import android.graphics.Bitmap
import com.example.freeapp_curvefever.Game.Model.Player.Player
import com.example.freeapp_curvefever.Game.Model.PowerUps.PowerUp

interface IView {
    abstract val backGroundColor: Int
    abstract var playerColor: Int
    abstract var playersNumber: Int
    abstract var powerUpStrings: List<String>
    abstract var lastFrameBuffer: Bitmap?
    abstract var debug: Boolean
    abstract val playerShip: Int
    abstract val roundNumber: Int

    fun drawTrails(players : List<Player>)
    fun drawHeads(players : List<Player>)
    fun drawPowerUps(powerUps : List<PowerUp>)
    fun normalizeX(x : Int) : Float
    fun normalizeY(y : Int) : Float
    fun saveFrameBuffer()
    fun updateAnimations(deltaTime: Float)
    fun drawDeathOf(p: Player)
    fun startNextRound()
    fun restartApplication()
}