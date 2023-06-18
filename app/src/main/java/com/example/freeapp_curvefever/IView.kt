package com.example.freeapp_curvefever

import android.graphics.Bitmap
import com.example.freeapp_curvefever.Model.Game.Game
import com.example.freeapp_curvefever.Model.Player.Player
import com.example.freeapp_curvefever.Model.PowerUps.PowerUp
import com.example.freeapp_curvefever.Utilities.Vector2

interface IView {
    abstract val backGroundColor: Int
    abstract var playerColor: Int
    abstract var playersNumber: Int
    abstract var powerUpStrings: List<String>
    abstract var lastFrameBuffer: Bitmap?
    abstract var debug: Boolean

    fun drawTrails(players : List<Player>)
    fun drawHeads(players : List<Player>)
    fun drawPowerUps(powerUps : List<PowerUp>)
    fun normalizeX(x : Int) : Float
    fun normalizeY(y : Int) : Float
    fun saveFrameBuffer()
    fun drawDeaths(deathCircles: MutableList<Game.Circle>)
    fun updateAnimations(deltaTime: Float)

    val playerShip: Int
}