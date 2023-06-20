package com.example.freeapp_curvefever.Game.Model.Player

import android.graphics.Bitmap
import android.graphics.Color
import com.example.freeapp_curvefever.Game.Assets
import com.example.freeapp_curvefever.Game.Model.PowerUps.PowerUp
import com.example.freeapp_curvefever.Game.Utilities.Vector2
import es.uji.vj1229.framework.AnimatedBitmap

open class Player constructor(
    val id : Int,
    var speed : Float,
    var rotationSpeed : Float,
    var radius : Float,
    val color : Int,
    var position : Vector2,
    var direction : Vector2,
    val icon : Bitmap,
    val animation : AnimatedBitmap,
    var lastPosition : Vector2 = position.copy(),
    var painting : Boolean = true,
    var timer : Float = 0f,
    var alive : Boolean = true,
    var lastNotPaintingStart : Float = timer,
    var lastPositionSavedTime : Float = timer,
    var activePowerUps : MutableList<PowerUp> = arrayListOf(),
    var immortal : Boolean = false,
    var deathCircle : Circle = Circle(position, 0f),
    var totalPoints : Int = 0,
    val startPosition : Vector2 = position.copy()
) {
    fun resetValues(){
        position = startPosition.copy()
        direction = Vector2.right
        lastPosition = position.copy()
        painting = true
        timer = 0f
        alive = true
        lastNotPaintingStart = timer
        lastPositionSavedTime = timer
        while(activePowerUps.isNotEmpty()){
            activePowerUps[0].uneffect()
        }
        immortal = false
        deathCircle = Circle(position, 0f)
    }

    fun collisionPoints(): List<Vector2> {
        return arrayListOf(
            position + direction * radius
            //position + direction.right() * radius,
            //position + direction.left() * radius
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
        return position.distanceTo(powerUp.center()) <= powerUp.bitmap.width+radius
    }

    fun updatePowerUps(deltaTime: Float) {
        for(powerUp in activePowerUps){
            powerUp.update(deltaTime)
        }
    }

    fun getExplosionSize() : Float{
        return radius * 4
    }

    fun addDeathCircle() {
        deathCircle = Circle(position.copy(), getExplosionSize())
    }

    override fun toString(): String {
        return id.toString()
    }
    class Circle constructor(val center : Vector2, val radius : Float)

    companion object{
        const val HOLE_SIZE = 0.5f
        const val HOLE_PROB = 0.1f
        const val SAVE_POSITION_DELAY = 0.3f
    }
}