package com.example.freeapp_curvefever.Model.PowerUps

import android.graphics.Bitmap
import android.util.Log
import com.example.freeapp_curvefever.Model.Player.Player
import com.example.freeapp_curvefever.Utilities.Vector2

interface PowerUp {
    var timer : Double
    var position : Vector2
    var radius : Double
    var player : Player?
    var duration : Float
    var bitmap : Bitmap
    val color : Int

    fun effect()
    fun uneffect()
    fun center() : Vector2

    fun catch(player : Player){
        player.activePowerUps.add(this)
        this.player = player
    }

    fun update(deltaTime : Float){
        timer += deltaTime
        if(timer >= duration){
            uneffect()
        }
        else{
            effect()
        }
    }
    fun copy() : PowerUp
}