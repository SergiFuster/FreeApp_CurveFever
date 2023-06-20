package com.example.freeapp_curvefever.Game.Model.Game

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.example.freeapp_curvefever.Game.Assets
import com.example.freeapp_curvefever.Game.Model.Player.NPC
import com.example.freeapp_curvefever.Game.Model.Player.Player
import com.example.freeapp_curvefever.Game.Model.PowerUps.*
import com.example.freeapp_curvefever.Game.Utilities.Vector2
import es.uji.vj1229.framework.AnimatedBitmap
import kotlin.random.Random

class Game private constructor(
    val players : List<Player>,
    val player : Player,
    val npcs : List<NPC>,
    val powerUpRadius : Float,
    var maxRounds : Int,
    private val availablePowerUps : List<PowerUp>,
    var rounds : Int = 0,
    val roundRanking : MutableList<Player> = arrayListOf(),
    val pointsPerRound : List<Int> = arrayListOf(25, 75, 125, 175, 225, 275),
    var winner : Player? = null
) {
    var onMapPowerUps : MutableList<PowerUp> = arrayListOf()

    fun collisions(frameBuffer: Bitmap, backgroundColor: Int){
        for (p in players) {
            if (!p.alive) {
                continue
            }
            collisionsWithPowerUps(p)
            if (collisionsWithLinesOrOutOfScreen(p, frameBuffer, backgroundColor)) {
                addPlayerToRoundRanking(p)
                addDeathCircleOfPlayer(p)
            }
        }
    }

    fun roundFinished() {
        rounds++
        for (i in 0 until roundRanking.size){
            roundRanking[i].totalPoints += pointsPerRound[i]
            Log.i("GAME", "Points for ${roundRanking[i]}: ${pointsPerRound[i]}, total: ${roundRanking[i].totalPoints}")
        }
        Log.i("GAME", "Rounds remaining: ${maxRounds - rounds}")
    }

    private fun addDeathCircleOfPlayer(p: Player) {
        p.addDeathCircle()
    }

    fun addPlayerToRoundRanking(p : Player){
        roundRanking.add(p)
    }

    fun addRemainingPlayerToRoundRanking(){
        for (player in players){
            if (player.alive){
                roundRanking.add(player)
            }
        }
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
            if (!p.alive) continue
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

    fun setReadyForNextRound() {
        for (p in players){
            p.resetValues()
        }
        roundRanking.clear()
        onMapPowerUps.clear()
    }

    fun setWinner() {
        var max = -1
        for(player in players){
            if (player.totalPoints > max){
                winner = player
                max = player.totalPoints
            }
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
        private var roundNumberBuilder : Int = 0
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
        fun setScreenSize(width : Int, height : Int) = apply{screenSize = Vector2(width, height) }
        fun setRoundNumber(value : Int) = apply { roundNumberBuilder = value }

        fun build() : Game {
            val availablePositions = mutableListOf<Vector2>(
                Vector2(0.375,  0.25) * screenSize,
                Vector2(0.625,  0.75) * screenSize,
                Vector2(0.625,  0.25) * screenSize,
                Vector2(0.375,  0.75) * screenSize,
                Vector2(0.75,   0.50) * screenSize,
                Vector2(0.25,   0.50) * screenSize
            )
            var id = 0
            val playerPosition : Vector2 = availablePositions.random()
            availablePositions.remove(playerPosition)
            val playerAnimation : AnimatedBitmap = Assets.getShipAnimationByIndex(playerShip)
            val mainPlayer : Player =
                Player(
                    id++,
                    playersSpeed,
                    playersRotationSpeed,
                    playersSize,
                    playerColor,
                    playerPosition,
                    Vector2.right,
                    playerAnimation
                )

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

            for(i in 1 until playersNumber) {
                val npcPosition : Vector2 = availablePositions.random()
                availablePositions.remove(npcPosition)
                val npcAnimation : AnimatedBitmap = Assets.getRandomAnimation()
                val npcColor = availableColors.random()
                availableColors.remove(npcColor)
                npcPlayers.add(
                    NPC(
                        id++,
                        playersSpeed,
                        playersRotationSpeed,
                        playersSize,
                        npcColor,
                        npcPosition,
                        Vector2.right,
                        npcAnimation
                    )
                )
            }

            val powerUps : MutableList<PowerUp> = arrayListOf()
            for (powerUpString in powerUpStrings){
                when(powerUpString){
                    "JUMP" -> {powerUps.add(Jump(Vector2.zero, powerUpSize.toDouble()))}
                    "SIZE_UP" -> {powerUps.add(SizeUp(Vector2.zero, powerUpSize.toDouble()))}
                    "SIZE_DOWN" -> {powerUps.add(SizeDown(Vector2.zero, powerUpSize.toDouble()))}
                    "SPEED_DOWN" -> {powerUps.add(SpeedDown(Vector2.zero, powerUpSize.toDouble()))}
                    "SPEED_UP" -> {powerUps.add(SpeedUp(Vector2.zero, powerUpSize.toDouble()))}
                }
            }

            val allPlayers : MutableList<Player> = arrayListOf()
            for (npc in npcPlayers) allPlayers.add(npc)
            allPlayers.add(mainPlayer)

            return Game(allPlayers, mainPlayer, npcPlayers, powerUpSize, roundNumberBuilder, powerUps.toList())
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