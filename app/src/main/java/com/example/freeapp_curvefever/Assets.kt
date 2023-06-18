package com.example.freeapp_curvefever

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import es.uji.vj1229.framework.AnimatedBitmap
import es.uji.vj1229.framework.SpriteSheet
import kotlin.math.roundToInt

object Assets {
    private const val SHIP6_SHEET_WIDTH = 215
    private const val SHIP6_SHEET_HEIGHT = 117

    private lateinit var ship6SpriteSheet : SpriteSheet
    lateinit var ship6Animation : AnimatedBitmap
    // region POWER UPS
    lateinit var puJumpIcon : Bitmap
    lateinit var puSizeDownIcon : Bitmap
    lateinit var puSizeUpIcon : Bitmap
    lateinit var puSpeedIcon : Bitmap
    // endregion

    private var availableIndexShipAnimations = arrayListOf(1, 2, 3, 4, 5, 6)


    fun createResizableAssets(context: Context, powerUpRadius: Float, playerSize: Float){
        val resources = context.resources

        val puSize : Int = (powerUpRadius * 2).roundToInt()

        puJumpIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_jump), puSize, puSize, true)
        puSizeDownIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_thick_down), puSize, puSize, true)
        puSizeUpIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_thick_up), puSize, puSize, true)
        puSpeedIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_speed), puSize, puSize, true)

        ship6SpriteSheet = SpriteSheet(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.ship_6
            ),
            SHIP6_SHEET_HEIGHT,
            SHIP6_SHEET_WIDTH
        )
        var ship6Width = (playerSize * 4).roundToInt()
        ship6Width += ship6Width % 2 // Round to an even
        val ratio : Float = SHIP6_SHEET_HEIGHT / SHIP6_SHEET_WIDTH.toFloat()
        Log.i("ASSETS", "ship6width: $ship6Width, ship6ratio: $ratio")
        var ship6Height : Int = (ship6Width * ratio).roundToInt()
        ship6Height += ship6Height % 2 // Round to an even

        val frames = ArrayList<Bitmap>()
        frames.clear()
        frames.addAll(ship6SpriteSheet.getScaledRow(0, 2, ship6Width, ship6Height))
        ship6Animation = AnimatedBitmap(0.2f, true, *frames.toTypedArray())

    }

    fun getShipAnimationByIndex(index : Int) : AnimatedBitmap{
        removeIndexFromAvailable(index)
        return when(index){
            // ...
            6 -> ship6Animation
            else -> ship6Animation
        }
    }

    // Temporal function
    fun getRandomAvailableIndex() : Int{
        return availableIndexShipAnimations.random()
    }
    private fun removeIndexFromAvailable(index : Int){
        availableIndexShipAnimations.remove(index)
    }

    fun AnimatedBitmap.rotatedCurrentFrame(degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.setRotate(degrees)
        return Bitmap.createBitmap(
            currentFrame,
            0,
            0,
            currentFrame.width,
            currentFrame.height,
            matrix,
            true
        )
    }

}