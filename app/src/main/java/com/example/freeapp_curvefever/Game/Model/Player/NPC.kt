package com.example.freeapp_curvefever.Game.Model.Player

import android.graphics.Bitmap
import android.graphics.Color
import com.example.freeapp_curvefever.Game.Assets
import com.example.freeapp_curvefever.Game.Utilities.Vector2
import com.jcamenatuji.sharkuji.controller.GestureDetector
import es.uji.vj1229.framework.AnimatedBitmap

class NPC(
    id : Int,
    speed: Float,
    rotationSpeed: Float,
    radius: Float,
    color: Int,
    position: Vector2,
    direction: Vector2,
    icon : Bitmap,
    animation : AnimatedBitmap
) : Player(id, speed, rotationSpeed, radius, color, position, direction, icon, animation) {
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
}