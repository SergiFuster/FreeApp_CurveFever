package com.example.freeapp_curvefever.Model.Player

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.get
import com.example.freeapp_curvefever.Assets
import com.example.freeapp_curvefever.Utilities.Vector2
import com.jcamenatuji.sharkuji.controller.GestureDetector
import es.uji.vj1229.framework.AnimatedBitmap
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class NPC(
    speed: Float,
    rotationSpeed: Float,
    radius: Float,
    color: Int,
    position: Vector2,
    direction: Vector2,
    animation : AnimatedBitmap
) : Player(speed, rotationSpeed, radius, color, position, direction, animation) {
    fun think(frameBuffer: Bitmap, backGroundColor : Int) {
        thinking = true

        var leftDangerDistanceAverage = 0f
        var rightDangerDistanceAverage = 0f
        var steps = 0
        for (degrees in 0 until 90 step 5){
            val leftDir = direction.copy()
            leftDir.rotate(degrees.toFloat(), false)
            val rightDir = direction.copy()
            rightDir.rotate(degrees.toFloat(), true)
            leftDangerDistanceAverage += distanceToDanger(frameBuffer, backGroundColor, leftDir)
            rightDangerDistanceAverage += distanceToDanger(frameBuffer, backGroundColor, rightDir)
            steps ++
        }
        leftDangerDistanceAverage /= steps
        rightDangerDistanceAverage /= steps
        when(minOf(leftDangerDistanceAverage, rightDangerDistanceAverage)){
            leftDangerDistanceAverage -> action = GestureDetector.Gestures.RIGHT
            rightDangerDistanceAverage -> action = GestureDetector.Gestures.LEFT
        }
        thinking = false
    }

    fun distanceToDanger(frameBuffer: Bitmap, backGroundColor: Int, direction: Vector2) : Float{
        var distance = radius / 2 + 1
        while (true){
            val pos = position + (direction * distance)
            try {
                if(frameBuffer.getPixel(pos.x.toInt(), pos.y.toInt()) != backGroundColor)
                    throw Exception()
            }
            catch (e : Exception){
                return distance
            }
            distance ++
        }
    }

    var action: GestureDetector.Gestures? = null
    var thinking: Boolean = false

    class Builder{
        private var speedBuilder : Float = 10f
        private var rotationSpeedBuilder : Float = 10f
        private var colorBuilder : Int = Color.WHITE
        private lateinit var animationBuilder : AnimatedBitmap
        private var positionBuilder : Vector2 = Vector2.zero
        private var directionBuilder : Vector2 = Vector2.right
        private var radiusBuilder : Float = 0f

        fun setSpeed(value : Float) = apply { speedBuilder = value }
        fun setRotationSpeed(value : Float) = apply { rotationSpeedBuilder = value }
        fun setColor(value : Int) = apply { colorBuilder = value }
        fun setShip(value : Int) = apply { animationBuilder = Assets.getShipAnimationByIndex(value) }
        fun setRadius(value : Float) = apply { radiusBuilder = value }
        fun setPosition(value : Vector2) = apply { positionBuilder = value }
        fun setDirection(value : Vector2) = apply { directionBuilder = value }

        fun build() : NPC{
            return NPC(
                speedBuilder,
                rotationSpeedBuilder,
                radiusBuilder,
                colorBuilder,
                positionBuilder,
                directionBuilder,
                animationBuilder
            )
        }
    }

    companion object{
        fun builder() : Builder = Builder()
    }
}