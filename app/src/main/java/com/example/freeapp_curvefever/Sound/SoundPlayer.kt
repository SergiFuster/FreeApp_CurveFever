package com.example.sharkuji.GAME.Sound

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.freeapp_curvefever.R

class SoundPlayer(val context : Context) {
    private val attributes = AudioAttributes.Builder().run{
        setUsage(AudioAttributes.USAGE_GAME)
        setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        build()
    }
    private val pool = SoundPool.Builder().run{
        setAudioAttributes(attributes)
        setMaxStreams(10)
        build()
    }
    private var volume : Float = 1f
    private var roundStart : Int = 0
    private var gameFinish : Int = 0
    private var collision : Int = 0
    private var powerup : Int = 0

    init {
        loadSounds()
    }

    private fun loadSounds(){
        roundStart = pool.load(context, R.raw.start, 1)
        gameFinish = pool.load(context, R.raw.finish, 1)
        collision = pool.load(context, R.raw.collision, 1)
        powerup = pool.load(context, R.raw.powerup, 1)
    }

    private fun playSound(id : Int){
        when(id) {
            roundStart -> pool.play(id, volume, volume, 0, 0, .5f)
            gameFinish -> pool.play(id, volume, volume, 0, 0, .5f)
            collision -> pool.play(id, volume, volume, 0, 0, 0.5f)
            powerup -> pool.play(id, volume, volume, 0, 0, 0.5f)
        }
    }

    fun Start(){
        playSound(roundStart)
    }

    fun Finish(){
        playSound(gameFinish)
    }

    fun Collision(){
        playSound(collision)
    }

    fun Powerup(){
        playSound(powerup)
    }
}
