package com.example.freeapp_curvefever

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.example.freeapp_curvefever.Controller.Controller
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
    override var playersNumber: Int = 6 // max 6
    override var powerUpStrings: List<String> = arrayListOf("JUMP")
    override val backGroundColor: Int = Color.DKGRAY
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

        Assets.createResizableAssets(this, powerUpCorrectedRadius)

        model =
            Model.builder()
                .setScreenSize(screenWidth, screenHeight)
                .setGame(
                    PLAYERS_SPEED / scaleX,
                    PLAYERS_ROTATION_SPEED,
                    playerColor,
                    playersNumber,
                    PLAYER_RADIUS / scaleX,
                    powerUpCorrectedRadius,
                    powerUpStrings
                )
                .build()


        if(::controller.isInitialized) controller.model = model
    }

    override fun drawTrails(players: List<Player>) {
        for(player in players) {
            with(player) {
                if (painting && !immortal) {
                    graphics.drawLine(
                        lastPosition.x.toFloat(),
                        lastPosition.y.toFloat(),
                        position.x.toFloat(),
                        position.y.toFloat(),
                        radius,
                        color
                    )
                }
            }
        }
    }

    override fun drawHeads(players: List<Player>) {
        for(player in players) {
            with(player) {
                graphics.drawCircle(
                    position.x.toFloat(),
                    position.y.toFloat(),
                    radius,
                    color
                )
            }
        }
    }

    override fun drawPowerUps(powerUps: List<PowerUp>) {
        for (powerUp in powerUps) {
            with(powerUp) {
                graphics.drawBitmap(
                    powerUp.bitmap,
                    position.x.toFloat(),
                    position.y.toFloat()
                )
            }
        }
    }

    override fun onDrawingRequested(): Bitmap {
        if (model != null) {
            if (lastFrameBuffer != null)
                graphics.drawBitmap(lastFrameBuffer, 0f, 0f)
        // region ---------------------------------DRAWING------------------------------------------
            drawTrails(model!!.game.players)
            lastFrameBuffer = graphics.frameBuffer.copy(graphics.frameBuffer.config, true)
            drawHeads(model!!.game.players)
            drawPowerUps(model!!.game.onMapPowerUps)
        // endregion -------------------------------------------------------------------------------
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
}