package com.example.freeapp_curvefever.Model.Game

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.example.freeapp_curvefever.Assets
import com.example.freeapp_curvefever.Model.Player.NPC
import com.example.freeapp_curvefever.Model.Player.Player
import com.example.freeapp_curvefever.Model.PowerUps.Jump
import com.example.freeapp_curvefever.Model.PowerUps.PowerUp
import com.example.freeapp_curvefever.Utilities.Vector2
import java.util.Stack
import kotlin.random.Random

class Game private constructor(
    val players : List<Player>,
    val player : Player,
    val npcs : List<NPC>,
    val ranking : Stack<Player>,
    val powerUpRadius : Float,
    private val availablePowerUps : List<PowerUp>,
    val deathCircles : MutableList<Circle> = arrayListOf()
) {
    var onMapPowerUps : MutableList<PowerUp> = arrayListOf()

    class Circle constructor(val center : Vector2, val radius : Float)
    fun collisions(frameBuffer: Bitmap, backgroundColor: Int){
        for (p in players) {
            if (!p.alive) continue
            collisionsWithPowerUps(p)
            if (collisionsWithLinesOrOutOfScreen(p, frameBuffer, backgroundColor))
                addDeathCircleOfPlayer(p)
        }
    }

    private fun addDeathCircleOfPlayer(p: Player) {
        deathCircles.add(Circle(p.position.copy(), p.getExplosionSize()))
    }

    private fun collisionsWithLinesOrOutOfScreen(p: Player, frameBuffer: Bitmap, backgroundColor: Int) : Boolean {
        for (point in p.collisionPoints()){
            if(pointCollideWithTrailOrIsOutOfScreen(frameBuffer, point, backgroundColor)){
                p.alive = false
                return true
            }
        }
        return false
    }

    private fun pointCollideWithTrailOrIsOutOfScreen(frameBuffer: Bitmap, point : Vector2, backgroundColor: Int) : Boolean{
        try {
            if (frameBuffer.getPixel(point.x.toInt(), point.y.toInt()) != backgroundColor)
                throw Exception()

        }
        catch (e : Exception){
            return true
        }
        return false
    }

    private fun collisionsWithPowerUps(player: Player) {
        val iterator = onMapPowerUps.iterator()
        while(iterator.hasNext()){
            val powerUp = iterator.next()
            if(player.collideWithPowerUp(powerUp)) {
                powerUp.catch(player)
                iterator.remove()
            }
        }
    }

    fun move(deltaTime: Float) {
        for(p in players) {
            if (p.timeToSaveLastPosition()) p.saveLastPosition()
            p.move(deltaTime)
        }
    }

    fun update(deltaTime: Float, screenWidth: Int, screeHeight: Int) {
        for(p in players){
            p.updateTimer(deltaTime)
            if (!p.painting) p.paintUpdate()
            p.tryStopPainting(deltaTime)
            p.updatePowerUps(deltaTime)
        }
        tryGeneratePowerUp(deltaTime, screenWidth, screeHeight)
    }

    private fun tryGeneratePowerUp(deltaTime: Float, screenWidth: Int, screeHeight: Int){
        if(Random.nextDouble() <= POWER_UP_PROB * deltaTime) generatePowerUp(screenWidth, screeHeight)
    }
    private fun generatePowerUp(screenWidth: Int, screeHeight: Int) {
        with(Random){
            val randomPos = Vector2(nextInt(0, screenWidth), nextInt(0, screeHeight))
            val chosenPowerUp : PowerUp = availablePowerUps.random().copy()
            chosenPowerUp.position = randomPos
            onMapPowerUps.add(chosenPowerUp)
        }
    }

    class Builder{
        private var playersSpeed : Float = 0f
        private var playersRotationSpeed : Float = 0f
        private var playerColor : Int = Color.WHITE
        private var playerShip : Int = 0
        private var playersNumber : Int = 0
        private var playersSize : Float = 0f
        private var powerUpSize : Float = 0f
        private lateinit var screenSize : Vector2
        private lateinit var powerUpStrings : List<String>

        fun setPlayersSpeed(value : Float) = apply { playersSpeed = value }
        fun setPlayersRotationSpeed(value : Float) = apply { playersRotationSpeed = value }
        fun setPlayerColor(value : Int) = apply { playerColor = value }
        fun setPlayerShip(value : Int) = apply {playerShip = value}
        fun setPlayersNumber(value : Int) = apply { playersNumber = value }
        fun setPlayersSize(value : Float) = apply {
            Log.i("GAME", "PLAYER SIZE : $value")
            playersSize = value }
        fun setPowerUpsString(value : List<String>) = apply { powerUpStrings = value }
        fun setPowerUpsSize(value : Float) = apply { powerUpSize = value }
        fun setScreenSize(width : Int, height : Int) = apply{screenSize = Vector2(width, height)}

        fun build() : Game{
            val availablePositions = mutableListOf<Vector2>(
                Vector2(0.375,  0.25) * screenSize,
                Vector2(0.625,  0.75) * screenSize,
                Vector2(0.625,  0.25) * screenSize,
                Vector2(0.375,  0.75) * screenSize,
                Vector2(0.75,   0.50) * screenSize,
                Vector2(0.25,   0.50) * screenSize

            )

            val mainPlayer : Player =
                Player.builder()
                    .setSpeed(playersSpeed)
                    .setRotationSpeed(playersRotationSpeed)
                    .setColor(playerColor)
                    .setShip(playerShip)
                    .setRadius(playersSize)
                    .setPosition(pickAndRemoveRandomPosition(availablePositions))
                    .build()



            val availableColors : MutableList<Int> =
                mutableListOf(
                    Color.RED,
                    Color.YELLOW,
                    Color.CYAN,
                    Color.GREEN,
                    Color.MAGENTA,
                    Color.BLUE
                )
            availableColors.remove(mainPlayer.color)

            val npcPlayers : MutableList<NPC> = arrayListOf()

            for(i in 1 until playersNumber)
                npcPlayers.add(
                    NPC.builder()
                        .setSpeed(playersSpeed)
                        .setRotationSpeed(playersRotationSpeed)
                        .setShip(Assets.getRandomAvailableIndex())
                        .setColor(pickAvailableColorAndDeleteFromList(availableColors))
                        .setRadius(playersSize)
                        .setPosition(pickAndRemoveRandomPosition(availablePositions))
                        .build()
                )

            val powerUps : MutableList<PowerUp> = arrayListOf()
            for (powerUpString in powerUpStrings){
                when(powerUpString){
                    "JUMP" -> {powerUps.add(Jump(Vector2.zero, powerUpSize.toDouble()))}
                    "SIZE_UP" -> {}
                    "SIZE_DOWN" -> {}
                    // ...
                }
            }

            val allPlayers : MutableList<Player> = arrayListOf()
            for (npc in npcPlayers) allPlayers.add(npc)
            allPlayers.add(mainPlayer)

            return Game(allPlayers, mainPlayer, npcPlayers, Stack<Player>(), powerUpSize, powerUps.toList())
        }

        private fun pickAndRemoveRandomPosition(availablePositions: MutableList<Vector2>): Vector2 {
            val randomIndex = Random.nextInt(availablePositions.size)
            val chosenPosition = availablePositions[randomIndex]

            availablePositions.removeAt(randomIndex)

            return chosenPosition
        }

        private fun pickAvailableColorAndDeleteFromList(availableColors : MutableList<Int>): Int {

            val randomIndex = Random.nextInt(availableColors.size)
            val chosenColor = availableColors[randomIndex]

            availableColors.removeAt(randomIndex)

            return chosenColor
        }
    }

    companion object{
        fun builder() : Builder = Builder()
        const val POWER_UP_PROB = 0.05
    }
}