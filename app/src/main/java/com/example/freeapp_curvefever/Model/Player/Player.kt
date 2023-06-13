package com.example.freeapp_curvefever.Model.Player

import android.graphics.Color
import android.util.Log
import com.example.freeapp_curvefever.Model.PowerUps.PowerUp
import com.example.freeapp_curvefever.Utilities.Vector2

open class Player protected constructor(
    var speed : Float,
    var rotationSpeed : Float,
    var radius : Float,
    val color : Int,
    var position : Vector2,
    var direction : Vector2,
    var lastPosition : Vector2 = position.copy(),
    var painting : Boolean = true,
    var timer : Float = 0f,
    var alive : Boolean = true,
    var lastNotPaintingStart : Float = timer,
    var lastPositionSavedTime : Float = timer,
    var activePowerUps : MutableList<PowerUp> = arrayListOf(),
    var immortal : Boolean = false
) {
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
        fun setRadius(value : Float) = apply {radiusBuilder = value }
        fun setPosition(value : Vector2) = apply { positionBuilder = value }
        fun setDirection(value : Vector2) = apply { directionBuilder = value }

        fun build() : Player{
            return Player(speedBuilder, rotationSpeedBuilder, radiusBuilder, colorBuilder, positionBuilder, directionBuilder)
        }
    }

    fun collisionPoints(): List<Vector2> {
        return arrayListOf(
            position + direction * radius,
            position + direction.right() * radius,
            position + direction.left() * radius
        )
    }

    fun timeToSaveLastPosition() : Boolean{
        return timer - lastPositionSavedTime >= SAVE_POSITION_DELAY
    }

    fun saveLastPosition(){
        position.copyInto(lastPosition)
        lastPositionSavedTime = timer
    }

    fun move(deltaTime : Float){
        position += direction * (speed * deltaTime)
    }

    fun paintUpdate() {
        if(timer - lastNotPaintingStart >= HOLE_SIZE){
            painting = true
        }
    }

    fun tryStopPainting(deltaTime: Float) {
        if (Math.random() <= HOLE_PROB * deltaTime) {
            lastNotPaintingStart = timer
            painting = false
        }
    }

    fun updateTimer(deltaTime: Float){
        timer += deltaTime
    }

    fun rotate(deltaTime : Float, right : Boolean){
        direction.rotate(rotationSpeed * deltaTime, right)
    }

    fun collideWithPowerUp(powerUp : PowerUp) : Boolean{
        return position.distanceTo(powerUp.center()) <= powerUp.radius+radius
    }

    fun updatePowerUps(deltaTime: Float) {
        for(powerUp in activePowerUps){
            powerUp.update(deltaTime)
        }
    }

    companion object{
        fun builder() : Builder = Builder()
        const val HOLE_SIZE = 0.5f
        const val HOLE_PROB = 0.1f
        const val SAVE_POSITION_DELAY = 0.2f
    }
}