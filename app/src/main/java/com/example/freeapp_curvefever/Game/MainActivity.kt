package com.example.freeapp_curvefever.Game

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import com.example.freeapp_curvefever.Game.Assets.rotatedCurrentFrame
import com.example.freeapp_curvefever.Game.Controller.Controller
import com.example.freeapp_curvefever.Game.Model.Model
import com.example.freeapp_curvefever.Game.Model.Player.Player
import com.example.freeapp_curvefever.Game.Model.PowerUps.PowerUp
import com.example.freeapp_curvefever.Game.Utilities.Vector2
import com.example.freeapp_curvefever.Menu.GameInfo
import com.example.freeapp_curvefever.Menu.MenuActivity
import es.uji.vj1229.framework.GameActivity
import es.uji.vj1229.framework.Graphics
import es.uji.vj1229.framework.IGameController

class MainActivity(

) : GameActivity(), IView {
    override var debug: Boolean = false

    // -------------------------------INTENT INFORMATION -------------------------------------------
    override var playerColor: Int = Color.RED
    override var playerShip: Int = 6 // from 1 to 6
    override var playersNumber: Int = 3 // max 6
    override var powerUpStrings: List<String> = arrayListOf("JUMP")
    override val backGroundColor: Int = Color.parseColor("#001B3B")
    override var roundNumber: Int = 1
    // ---------------------------------------------------------------------------------------------
    override var lastFrameBuffer: Bitmap? = null
    var model : Model? = null
    private var screenWidth : Int = 0
    private var screenHeight : Int = 0
    private var scaleX : Float = 0f
    private var scaleY : Float = 0f

    private lateinit var controller : Controller
    lateinit var graphics : Graphics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landscapeFullScreenOnCreate()
        val gameInfo : GameInfo? = intent.getParcelableExtra(GAME_INFO)

        if(gameInfo != null){
            playerColor = gameInfo.playerColor
            playerShip = gameInfo.shipIndex
            playersNumber = gameInfo.nPlayers
            powerUpStrings = gameInfo.powerUps
            roundNumber = gameInfo.nRounds
        }
    }

    override fun onBitmapMeasuresAvailable(width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
        scaleX = STAGE_WIDTH.toFloat() / screenWidth
        scaleY = STAGE_HEIGHT.toFloat() / screenHeight
        graphics = Graphics(screenWidth, screenHeight)
        graphics.clear(backGroundColor)

        val powerUpCorrectedRadius = POWER_UP_RADIUS / scaleX
        val playerSize = PLAYER_RADIUS / scaleX
        Assets.createResizableAssets(this, screenWidth, powerUpCorrectedRadius, playerSize)
        model =
            Model.builder()
                .setScreenSize(screenWidth, screenHeight)
                .setGame(
                    PLAYERS_SPEED / scaleX,
                    PLAYERS_ROTATION_SPEED,
                    playerColor,
                    playerShip,
                    playersNumber,
                    playerSize,
                    powerUpCorrectedRadius,
                    powerUpStrings,
                    roundNumber
                )
                .build()


        if(::controller.isInitialized) controller.model = model
    }

    override fun drawTrails(players: List<Player>) {
        for(player in players) {
            with(player) {
                if (painting && !immortal && alive) {
                    drawLine(graphics, lastPosition, position, radius, color)
                    drawCircle(graphics, position, radius/2, color)
                    drawCircle(graphics, lastPosition, radius/2, color)
                }
                if(!player.alive)
                    drawDeathOf(player)
            }
        }
    }

    private fun drawLine(graphics: Graphics, src : Vector2, dst : Vector2, radius : Float, color : Int){
        graphics.drawLine(
            src.x.toFloat(),
            src.y.toFloat(),
            dst.x.toFloat(),
            dst.y.toFloat(),
            radius,
            color
        )
    }
    private fun drawCircle(graphics: Graphics, position : Vector2, radius : Float, color : Int){
        graphics.drawCircle(
            position.x.toFloat(),
            position.y.toFloat(),
            radius,
            color
        )
    }

    override fun drawHeads(players: List<Player>) {
        for(player in players) {
            with(player) {
                val rotatedBitmap = animation.rotatedCurrentFrame(com.example.freeapp_curvefever.Game.Utilities.Vector2.degreesBetween(direction).toFloat())
                graphics.drawBitmap(
                    rotatedBitmap,
                    position.x.toFloat() - rotatedBitmap.width / 2,
                    position.y.toFloat() - rotatedBitmap.height / 2
                )
            }
        }
    }

    override fun drawPowerUps(powerUps: List<PowerUp>) {
        for (powerUp in powerUps) {
            with(powerUp) {
                val w = bitmap.width.toFloat()
                val h = bitmap.height.toFloat()
                drawCircle(graphics, position, w, color)
                graphics.drawBitmap(
                    bitmap,
                    position.x.toFloat() - w/2,
                    position.y.toFloat() - h/2
                )
            }
        }
    }

    override fun onDrawingRequested(): Bitmap {
        if (model != null) {
            if(model!!.state != Model.State.PLAYING) graphics.clear(backGroundColor)

            if (lastFrameBuffer != null && model!!.state == Model.State.PLAYING)
                graphics.drawBitmap(lastFrameBuffer, 0f, 0f)

            when(model!!.state){
                Model.State.STARTING -> {
                    graphics.setTextSize(100)
                    graphics.drawText(screenWidth / 2f, screenHeight / 2f, (controller.timeToStart-controller.timer).toInt().toString())
                    drawHeads(model!!.game.players)
                }
                Model.State.RESULTS -> {
                    drawResults()
                }
                Model.State.FINISHED ->{
                    drawWinner()
                    drawFinishMessage()
                }
                Model.State.PLAYING -> {
                    drawTrails(model!!.game.players)
                    controller.saveFrameBufferIfMandatory()
                    drawHeads(model!!.game.players)
                    drawPowerUps(model!!.game.onMapPowerUps)
                    drawRounds()
                }
            }
        }
        return graphics.frameBuffer
    }

    private fun drawRounds() {
        val xPos = 0f
        val yPos = screenHeight.toFloat()
        graphics.setTextSize(20)
        graphics.drawText(xPos, yPos, "Round ${model!!.game.rounds} of $roundNumber")
    }

    private fun drawFinishMessage() {
        val text = "Touch the screen to restart the game"
        val xPos = screenWidth / 4f
        val yPos = (3f/4f) * screenHeight
        graphics.drawText(xPos, yPos, text)
    }

    private fun drawWinner() {
        graphics.drawBitmap(Assets.crown, screenWidth/4f, screenHeight/4f)
        graphics.setTextSize(50)
        graphics.drawText(screenWidth/4f+ Assets.crown.width, screenHeight/4f + 50f, "WINNER")
        graphics.drawBitmap(
            model!!.game.winner!!.animation.currentFrame,
            screenWidth/2f,
            screenHeight/4f
        )
    }

    private fun drawResults() {

        graphics.clear(backGroundColor)

        val xPos = screenWidth / 4f
        var yPos = 0f
        graphics.setTextSize(50)
        for(i in 0 until model!!.game.players.size){
            yPos = (100 * (i+1)).toFloat() + 10
            graphics.drawBitmap(
                model!!.game.players[i].animation.currentFrame,
                xPos,
                yPos - model!!.game.players[i].animation.currentFrame.height
            )
            graphics.drawText(
                xPos + model!!.game.players[i].animation.currentFrame.width,
                yPos,
                "Points: ${model!!.game.players[i].totalPoints}"
            )
        }
    }

    override fun buildGameController(): IGameController {
        controller = Controller(this, model)
        return controller
    }

    companion object{
        const val STAGE_WIDTH = 480
        const val STAGE_HEIGHT = 320
        const val PLAYER_RADIUS = STAGE_WIDTH / 100
        const val POWER_UP_RADIUS = STAGE_WIDTH / 40
        const val PLAYERS_SPEED = 48
        const val PLAYERS_ROTATION_SPEED = 100f
        const val GAME_INFO = "GAME_INFO"
    }

    override fun normalizeX(x: Int) : Float {
        return x * scaleX
    }

    override fun normalizeY(y: Int) : Float {
        return y * scaleY
    }

    override fun saveFrameBuffer() {
        lastFrameBuffer = graphics.frameBuffer.copy(graphics.frameBuffer.config, true)
    }

    override fun drawDeathOf(p : Player) {
        drawCircle(graphics, p.deathCircle.center, p.deathCircle.radius, backGroundColor)
    }

    override fun startNextRound() {
        graphics.clear(backGroundColor)
        lastFrameBuffer = graphics.frameBuffer.copy(graphics.frameBuffer.config, true)
    }

    override fun restartApplication() {
        val intent = Intent(this, MenuActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.startActivity(intent)
        this.finish()
    }

    override fun updateAnimations(deltaTime: Float) {
        for (player in model!!.game.players)
            player.animation.update(deltaTime)
    }
}
