package com.example.freeapp_curvefever.Game.Model.PowerUps

import android.graphics.Bitmap
import android.graphics.Color
import com.example.freeapp_curvefever.Game.Assets
import com.example.freeapp_curvefever.Game.Model.Player.Player
import com.example.freeapp_curvefever.Game.Utilities.Vector2

class SizeDown(
    override var position: Vector2,
    override var radius: Double,
    override var player: Player? = null,
    override var duration: Float = 4f
) : PowerUp {
    override var timer: Double = 0.0
    override var bitmap: Bitmap = Assets.puSizeUpIcon
    override val color: Int = Color.argb(200, 145, 80, 134)

    override fun effect() {
        if(player != null)
            player!!.radius /= 2
    }

    override fun uneffect() {
        if(player != null)
            player!!.radius *= 2
        player?.activePowerUps?.remove(this)
    }

    override fun center(): Vector2 {
        return Vector2(position.x + bitmap.width/2, position.y + bitmap.height/2)
    }

    override fun copy(): PowerUp {
        return SizeDown(position, radius, player, duration)
    }
}