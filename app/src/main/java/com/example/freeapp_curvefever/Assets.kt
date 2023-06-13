package com.example.freeapp_curvefever

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.freeapp_curvefever.Model.Game.Game
import kotlin.math.roundToInt

object Assets {

    // region POWER UPS
    lateinit var pu_jump_icon : Bitmap
    lateinit var pu_size_down_icon : Bitmap
    lateinit var pu_size_up_icon : Bitmap
    lateinit var pu_speed_icon : Bitmap
    // endregion

    fun createResizableAssets(context : Context, powerUpRadius : Float){
        val resources = context.resources

        val pu_size : Int = (powerUpRadius * 2).roundToInt()

        pu_jump_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_jump), pu_size, pu_size, true)
        pu_size_down_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_size_down), pu_size, pu_size, true)
        pu_size_up_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_size_up), pu_size, pu_size, true)
        pu_speed_icon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
        R.drawable.powerup_speed), pu_size, pu_size, true)
    }

}