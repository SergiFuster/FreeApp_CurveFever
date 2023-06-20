package com.example.freeapp_curvefever.Game.Model.PowerUps

import android.graphics.Bitmap
import com.example.freeapp_curvefever.Game.Model.Player.Player
import com.example.freeapp_curvefever.Game.Utilities.Vector2

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
        effect()
    }

    fun update(deltaTime : Float){
        timer += deltaTime
        if(timer >= duration){
            uneffect()
        }
    }
    fun copy() : PowerUp
}