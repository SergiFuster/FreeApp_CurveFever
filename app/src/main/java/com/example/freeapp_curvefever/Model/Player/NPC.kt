package com.example.freeapp_curvefever.Model.Player

import android.graphics.Color
import com.example.freeapp_curvefever.Utilities.Vector2

class NPC(
    speed: Float,
    rotationSpeed: Float,
    radius: Float,
    color: Int,
    position: Vector2,
    direction: Vector2
) : Player(speed, rotationSpeed, radius, color, position, direction) {
    class Builder{
        private var speedBuilder : Float = 10f
        private var rotationSpeedBuilder : Float = 10f
        private var colorBuilder : Int = Color.WHITE
        private var positionBuilder : Vector2 = Vector2.zero
        private var directionBuilder : Vector2 = Vector2.right
        private var radiusBuilder : Float = 0f

        fun setSpeed(value : Float) = apply { speedBuilder = value }
        fun setRotationSpeed(value : Float) = apply { rotationSpeedBuilder = value }
        fun setColor(value : Int) = apply { colorBuilder = value }
        fun setRadius(value : Float) = apply { radiusBuilder = value }
        fun setPosition(value : Vector2) = apply { positionBuilder = value }
        fun setDirection(value : Vector2) = apply { directionBuilder = value }

        fun build() : Player{
            return NPC(speedBuilder, rotationSpeedBuilder, radiusBuilder, colorBuilder, positionBuilder, directionBuilder)
        }
    }

    companion object{
        fun builder() : Builder = Builder()
    }
}