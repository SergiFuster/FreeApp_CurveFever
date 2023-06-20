package com.example.freeapp_curvefever.Game

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Size
import com.example.freeapp_curvefever.R
import es.uji.vj1229.framework.AnimatedBitmap
import es.uji.vj1229.framework.SpriteSheet
import kotlin.math.roundToInt

object Assets {
    private const val SHIP1_SHEET_WIDTH = 511/3
    private const val SHIP1_SHEET_HEIGHT = 237
    private const val SHIP2_SHEET_WIDTH = 465/3
    private const val SHIP2_SHEET_HEIGHT = 256
    private const val SHIP3_SHEET_WIDTH = 385/3
    private const val SHIP3_SHEET_HEIGHT = 256
    private const val SHIP4_SHEET_WIDTH = 307/3
    private const val SHIP4_SHEET_HEIGHT = 279
    private const val SHIP5_SHEET_WIDTH = 336/3
    private const val SHIP5_SHEET_HEIGHT = 235
    private const val SHIP6_SHEET_WIDTH = 215
    private const val SHIP6_SHEET_HEIGHT = 117

    private lateinit var ship1SpriteSheet : SpriteSheet
    private lateinit var ship2SpriteSheet : SpriteSheet
    private lateinit var ship3SpriteSheet : SpriteSheet
    private lateinit var ship4SpriteSheet : SpriteSheet
    private lateinit var ship5SpriteSheet : SpriteSheet
    private lateinit var ship6SpriteSheet : SpriteSheet
    lateinit var ship1Animation : AnimatedBitmap
    lateinit var ship2Animation : AnimatedBitmap
    lateinit var ship3Animation : AnimatedBitmap
    lateinit var ship4Animation : AnimatedBitmap
    lateinit var ship5Animation : AnimatedBitmap
    lateinit var ship6Animation : AnimatedBitmap

    lateinit var puJumpIcon : Bitmap
    lateinit var puSizeDownIcon : Bitmap
    lateinit var puSizeUpIcon : Bitmap
    lateinit var puSpeedIcon : Bitmap

    lateinit var ship1 : Bitmap
    lateinit var ship2 : Bitmap
    lateinit var ship3 : Bitmap
    lateinit var ship4 : Bitmap
    lateinit var ship5 : Bitmap
    lateinit var ship6 : Bitmap

    lateinit var  crown : Bitmap

    lateinit var iconsAndAnimations : MutableList<IconAndAnimation>


    fun createResizableAssets(context: Context, screenWidth : Int, powerUpRadius: Float, playerSize: Float){
        val resources = context.resources

        val puSize : Int = (powerUpRadius * 2).roundToInt()
        val crownSize : Int = (playerSize * 8).roundToInt()
        val ship1Size = getRelativeShipSize(playerSize, SHIP1_SHEET_HEIGHT, SHIP1_SHEET_WIDTH)
        val ship2Size = getRelativeShipSize(playerSize, SHIP2_SHEET_HEIGHT, SHIP2_SHEET_WIDTH)
        val ship3Size = getRelativeShipSize(playerSize, SHIP3_SHEET_HEIGHT, SHIP3_SHEET_WIDTH)
        val ship4Size = getRelativeShipSize(playerSize, SHIP4_SHEET_HEIGHT, SHIP4_SHEET_WIDTH)
        val ship5Size = getRelativeShipSize(playerSize, SHIP5_SHEET_HEIGHT, SHIP5_SHEET_WIDTH)
        val ship6Size = getRelativeShipSize(playerSize, SHIP6_SHEET_HEIGHT, SHIP6_SHEET_WIDTH)
        puJumpIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.powerup_jump
        ), puSize, puSize, true)
        puSizeDownIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.powerup_thick_down
        ), puSize, puSize, true)
        puSizeUpIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.powerup_thick_up
        ), puSize, puSize, true)
        puSpeedIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.powerup_speed_up
        ), puSize, puSize, true)
        crown = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.crown
        ), crownSize, crownSize, true)
        ship1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.ship_1_icon
        ), (playerSize*4).toInt(), (playerSize*4).toInt(), true)
        ship2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.ship_2_icon
        ), (playerSize*4).toInt(), (playerSize*4).toInt(), true)
        ship3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.ship_3_icon
        ), (playerSize*4).toInt(), (playerSize*4).toInt(), true)
        ship4 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.ship_4_icon
        ), (playerSize*4).toInt(), (playerSize*4).toInt(), true)
        ship5 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.ship_5_icon
        ), (playerSize*4).toInt(), (playerSize*4).toInt(), true)
        ship6 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
            R.drawable.ship_6_icon
        ), ship6Size.width, ship6Size.height, true)

        ship1SpriteSheet = createSpriteSheet(resources, R.drawable.ship_1, SHIP1_SHEET_HEIGHT, SHIP1_SHEET_WIDTH)
        ship2SpriteSheet = createSpriteSheet(resources, R.drawable.ship_2, SHIP2_SHEET_HEIGHT, SHIP2_SHEET_WIDTH)
        ship3SpriteSheet = createSpriteSheet(resources, R.drawable.ship_3, SHIP3_SHEET_HEIGHT, SHIP3_SHEET_WIDTH)
        ship4SpriteSheet = createSpriteSheet(resources, R.drawable.ship_4, SHIP4_SHEET_HEIGHT, SHIP4_SHEET_WIDTH)
        ship5SpriteSheet = createSpriteSheet(resources, R.drawable.ship_5, SHIP5_SHEET_HEIGHT, SHIP5_SHEET_WIDTH)
        ship6SpriteSheet = createSpriteSheet(resources, R.drawable.ship_6, SHIP6_SHEET_HEIGHT, SHIP6_SHEET_WIDTH)



        val frames = ArrayList<Bitmap>()
        formatFrames(frames, ship1SpriteSheet, 0, 3, ship1Size)
        ship1Animation = AnimatedBitmap(0.2f, true, *frames.toTypedArray())
        formatFrames(frames, ship2SpriteSheet, 0, 3, ship2Size)
        ship2Animation = AnimatedBitmap(0.2f, true, *frames.toTypedArray())
        formatFrames(frames, ship3SpriteSheet, 0, 3, ship3Size)
        ship3Animation = AnimatedBitmap(0.2f, true, *frames.toTypedArray())
        formatFrames(frames, ship4SpriteSheet, 0, 3, ship4Size)
        ship4Animation = AnimatedBitmap(0.2f, true, *frames.toTypedArray())
        formatFrames(frames, ship5SpriteSheet, 0, 3, ship5Size)
        ship5Animation = AnimatedBitmap(0.2f, true, *frames.toTypedArray())
        formatFrames(frames, ship6SpriteSheet, 0, 2, ship6Size)
        ship6Animation = AnimatedBitmap(0.2f, true, *frames.toTypedArray())

        iconsAndAnimations = arrayListOf(
            IconAndAnimation(ship1, ship1Animation),
            IconAndAnimation(ship2, ship2Animation),
            IconAndAnimation(ship3, ship3Animation),
            IconAndAnimation(ship4, ship4Animation),
            IconAndAnimation(ship5, ship5Animation),
            IconAndAnimation(ship6, ship6Animation)
        )
    }

    private fun formatFrames(frames : ArrayList<Bitmap>, sSheet : SpriteSheet, rows : Int, columns : Int, size : Size){
        frames.clear()
        frames.addAll(sSheet.getScaledRow(rows, columns, size.width, size.height))
    }
    private fun getRelativeShipSize(playerSize: Float, shipHeight : Int, shipWidth : Int) : Size{

        var width = (playerSize * 4).roundToInt()
        width += width % 2 // Round to an even
        val ratio : Float = shipHeight / shipWidth.toFloat()
        var height : Int = (width * ratio).roundToInt()
        height += height % 2 // Round to an even
        return Size(width, height)
    }
    private fun createSpriteSheet(resources: Resources?, id: Int, elemHeight: Int, elemWidth: Int) : SpriteSheet{
        return SpriteSheet(
            BitmapFactory.decodeResource(
                resources,
                id
            ),
            elemHeight,
            elemWidth
        )
    }

    fun getShipAnimationByIndex(index : Int) : IconAndAnimation{
        val anim = iconsAndAnimations[index-1]
        iconsAndAnimations.remove(anim)
        return anim
    }

    fun getRandomAnimation() : IconAndAnimation{
        val anim = iconsAndAnimations.random()
        iconsAndAnimations.remove(anim)
        return anim
    }

    fun AnimatedBitmap.rotatedCurrentFrame(degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.setRotate(degrees)
        return Bitmap.createBitmap(
            currentFrame,
            0,
            0,
            currentFrame.width,
            currentFrame.height,
            matrix,
            true
        )
    }

    fun Bitmap.rotated(degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.setRotate(degrees)
        return Bitmap.createBitmap(
            this,
            0,
            0,
            this.width,
            this.height,
            matrix,
            true
        )
    }

    class IconAndAnimation constructor(
        val icon : Bitmap,
        val animation : AnimatedBitmap
    )

}