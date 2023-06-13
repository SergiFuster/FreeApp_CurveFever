package com.example.freeapp_curvefever.Model

import android.graphics.Bitmap
import com.example.freeapp_curvefever.Model.Game.Game
import com.jcamenatuji.sharkuji.controller.GestureDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Model private constructor(
    val game : Game,
    val screenWidth : Int,
    val screeHeight : Int,
    var timer : Float = 0f,
    var lastFrameBufferSave : Float = 0f
){

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
            playersNumber : Int,
            playersSize : Float,
            powerUpsSize : Float,
            powerUpStrings : List<String>
        ) = apply {
            game =
                Game.builder()
                    .setPlayersSpeed(playersSpeed)
                    .setPlayersRotationSpeed(playersRotationSpeed)
                    .setPlayerColor(playerColor)
                    .setPlayersNumber(playersNumber)
                    .setPlayersSize(playersSize)
                    .setPowerUpsSize(powerUpsSize)
                    .setPowerUpsString(powerUpStrings)
                    .setScreenSize(screenWidthBuilder, screenHeightBuilder)
                    .build()
        }

        fun build() : Model{
            return Model(game, screenWidthBuilder, screenHeightBuilder)
        }
    }

    fun update(deltaTime: Float, gesture: GestureDetector.Gestures?){
        timer += deltaTime
        if(gesture != null){
            game.player.rotate(deltaTime, gesture == GestureDetector.Gestures.RIGHT)
        }
        executeNPCsActions(deltaTime)
        game.move(deltaTime)
        game.update(deltaTime,screenWidth, screeHeight)
    }

    private fun executeNPCsActions(deltaTime: Float) {
        for (npc in game.npcs){
            if (npc.action == null) continue
            npc.rotate(deltaTime, npc.action == GestureDetector.Gestures.RIGHT)
            npc.action = null
        }

    }

    fun saveFrameBufferIfMandatory(viewSaveLastFrameBuffer : () -> Unit){
        if (timer-lastFrameBufferSave >= TIME_TO_SAVE_FRAME_BUFFER) {
            lastFrameBufferSave = timer
            viewSaveLastFrameBuffer()
        }
    }

    fun collisions(frameBuffer : Bitmap, backgroundColor : Int){
        game.collisions(frameBuffer, backgroundColor)
    }

    fun think(lastFrameBuffer: Bitmap?) {
        if (lastFrameBuffer == null) return
        for(npc in game.npcs){
            if (npc.thinking) continue
            CoroutineScope(Dispatchers.Main).launch {
                npc.think(lastFrameBuffer)
            }
        }
    }

    companion object{
        fun builder() : Builder = Builder()
        const val TIME_TO_SAVE_FRAME_BUFFER = 1 / 10
    }

}
