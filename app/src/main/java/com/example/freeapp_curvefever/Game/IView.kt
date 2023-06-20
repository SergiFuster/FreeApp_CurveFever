package com.example.freeapp_curvefever.Game

import android.graphics.Bitmap
import com.example.freeapp_curvefever.Game.Model.Player.Player
import com.example.freeapp_curvefever.Game.Model.PowerUps.PowerUp
import com.example.sharkuji.GAME.Sound.SoundPlayer

interface IView {
    val backGroundColor: Int
    var playerColor: Int
    var playersNumber: Int
    var powerUpStrings: List<String>
    var lastFrameBuffer: Bitmap?
    var debug: Boolean
    val playerShip: Int
    val roundNumber: Int
    var sound: Boolean
    var soundEffects: SoundPlayer?

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
    fun startMusic()
}