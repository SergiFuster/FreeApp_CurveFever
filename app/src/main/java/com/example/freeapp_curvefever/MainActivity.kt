package com.example.freeapp_curvefever

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.example.freeapp_curvefever.Assets.rotatedCurrentFrame
import com.example.freeapp_curvefever.Controller.Controller
import com.example.freeapp_curvefever.Model.Game.Game
import com.example.freeapp_curvefever.Model.Model
import com.example.freeapp_curvefever.Model.Player.Player
import com.example.freeapp_curvefever.Model.PowerUps.PowerUp
import com.example.freeapp_curvefever.Utilities.Vector2
import es.uji.vj1229.framework.GameActivity
import es.uji.vj1229.framework.Graphics
import es.uji.vj1229.framework.IGameController

class MainActivity(

) : GameActivity(), IView {
    override var debug: Boolean = false

    // -------------------------------INTENT INFORMATION -------------------------------------------
    override var playerColor: Int = Color.RED
    override val playerShip: Int = 6
    override var playersNumber: Int = 1 // max 6
    override var powerUpStrings: List<String> = arrayListOf("JUMP")
    override val backGroundColor: Int = Color.parseColor("#001B3B")
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
        Assets.createResizableAssets(this, powerUpCorrectedRadius, playerSize)
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
                    powerUpStrings
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
                val rotatedBitmap = animation.rotatedCurrentFrame(Vector2.degreesBetween(direction).toFloat())
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

            if (lastFrameBuffer != null)
                graphics.drawBitmap(lastFrameBuffer, 0f, 0f)

            drawTrails(model!!.game.players)
            drawDeaths(model!!.game.deathCircles)
            controller.saveFrameBufferIfMandatory()
            drawHeads(model!!.game.players)
            drawPowerUps(model!!.game.onMapPowerUps)
        }



        return graphics.frameBuffer
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

    override fun drawDeaths(deathCircles: MutableList<Game.Circle>) {
        for (circle in deathCircles){
            drawCircle(graphics, circle.center, circle.radius, backGroundColor)
        }
    }

    override fun updateAnimations(deltaTime: Float) {
        for (player in model!!.game.players)
            player.animation.update(deltaTime)
    }
}
