package com.example.freeapp_curvefever.Model.PowerUps

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.example.freeapp_curvefever.Assets
import com.example.freeapp_curvefever.Model.Player.Player
import com.example.freeapp_curvefever.Utilities.Vector2

class Jump(
    override var position: Vector2,
    override var radius: Double,
    override var player: Player? = null,
    override var duration: Float = 1.5f
) : PowerUp {
    override var timer: Double = 0.0
    override var bitmap: Bitmap = Assets.puJumpIcon
    override val color: Int = Color.argb(200, 145, 80, 134)

    override fun effect() {
        player?.immortal = true
    }

    override fun uneffect() {
        player?.immortal = false
        player?.activePowerUps?.remove(this)
    }

    override fun center(): Vector2 {
        return Vector2(position.x + bitmap.width/2, position.y + bitmap.height/2)
    }

    override fun copy(): PowerUp {
        return Jump(position, radius, player, duration)
    }

    /**
     * This functions checks if this powerup collides
     * with a player, if so, save the reference to
     * it and return a boolean indicating if collided
     * @param player The player which are checking for collisions
     * @return The state of the collision
     */
}