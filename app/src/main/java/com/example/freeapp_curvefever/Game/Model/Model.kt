package com.example.freeapp_curvefever.Game.Model

import android.graphics.Bitmap
import com.example.freeapp_curvefever.Game.Model.Game.Game
import com.jcamenatuji.sharkuji.controller.GestureDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Model private constructor(
    val game : Game,
    val screenWidth : Int,
    val screeHeight : Int,
    var state : State = State.STARTING
){
    enum class State{
        STARTING,
        PLAYING,
        RESULTS,
        FINISHED
    }
    class Builder{

        private lateinit var game : Game
        private var screenWidthBuilder : Int = 0
        private var screenHeightBuilder : Int = 0

        fun setScreenSize(width : Int, height : Int) = apply {
            screenWidthBuilder = width
            screenHeightBuilder = height
        }
        fun setGame(
            playersSpeed : Float,
            playersRotationSpeed : Float,
            playerColor : Int,
            playerShipIndex : Int,
            playersNumber : Int,
            playersSize : Float,
            powerUpsSize : Float,
            powerUpStrings : List<String>,
            roundNumber : Int
        ) = apply {
            game =
                Game.builder()
                    .setPlayersSpeed(playersSpeed)
                    .setPlayersRotationSpeed(playersRotationSpeed)
                    .setPlayerColor(playerColor)
                    .setPlayerShip(playerShipIndex)
                    .setPlayersNumber(playersNumber)
                    .setPlayersSize(playersSize)
                    .setPowerUpsSize(powerUpsSize)
                    .setPowerUpsString(powerUpStrings)
                    .setScreenSize(screenWidthBuilder, screenHeightBuilder)
                    .setRoundNumber(roundNumber)
                    .build()
        }

        fun build() : Model {
            return Model(game, screenWidthBuilder, screenHeightBuilder)
        }
    }

    fun startNextRound() {
        game.setReadyForNextRound()
        state = State.STARTING
    }

    fun finishGame() {
        state = State.FINISHED
        game.setWinner()
    }

    fun updateGame(deltaTime: Float){
        game.update(deltaTime, screenWidth, screeHeight)
    }

    fun movement(deltaTime: Float){
        game.move(deltaTime)
    }

    fun rotations(deltaTime: Float, gesture: GestureDetector.Gestures?){
        if(gesture != null && game.player.alive){
            game.player.rotate(deltaTime, gesture == GestureDetector.Gestures.RIGHT)
        }
        executeNPCsActions(deltaTime)
    }

    private fun executeNPCsActions(deltaTime: Float) {
        for (npc in game.npcs){
            if (npc.action == null || !npc.alive) continue
            npc.rotate(deltaTime, npc.action == GestureDetector.Gestures.RIGHT)
            npc.action = null
        }

    }

    fun collisions(frameBuffer : Bitmap, backgroundColor : Int){
        game.collisions(frameBuffer, backgroundColor)
    }

    fun roundFinished(){
        game.addRemainingPlayerToRoundRanking()
        game.roundFinished()
        state = State.RESULTS
    }

    fun think(lastFrameBuffer: Bitmap?, backgroundColor: Int) {
        if (lastFrameBuffer == null) return
        for(npc in game.npcs){
            if (npc.thinking || !npc.alive) continue
            CoroutineScope(Dispatchers.Main).launch {
                npc.think(lastFrameBuffer, backgroundColor)
            }
        }
    }

    companion object{
        fun builder() : Builder = Builder()
        const val TIME_TO_SAVE_FRAME_BUFFER = 1 / 10
    }

}
