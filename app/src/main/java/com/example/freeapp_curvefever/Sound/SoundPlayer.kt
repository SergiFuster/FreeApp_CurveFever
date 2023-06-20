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
    private var shellCaughtId : Int = 0
    private var preyCaughtId : Int = 0
    private var explosionId : Int = 0
    private var deathId : Int = 0
    private var finId : Int = 0

    init {
        loadSounds()
    }

    private fun loadSounds(){
        /*
        shellCaughtId = pool.load(context, R.raw.picked_shell, 1)
        preyCaughtId = pool.load(context, R.raw.prey_caught, 1)
        explosionId = pool.load(context, R.raw.explosion, 1)
        deathId = pool.load(context, R.raw.death, 1)
        finId = pool.load(context, R.raw.fin, 1)
        */
    }

    private fun playSound(id : Int){
        when(id) {
            finId -> pool.play(id, 0.05f, 0.05f, 0, 0, .5f)
            deathId -> pool.play(id, volume, volume, 0, 0, .5f)
            else -> pool.play(id, volume, volume, 0, 0, 1f)
        }
    }

    fun Shell(){
        playSound(shellCaughtId)
    }

    fun Prey(){
        playSound(preyCaughtId)
    }

    fun Explosion(){
        playSound(explosionId)
    }

    fun Death(){
        playSound(deathId)
    }

    fun Fin(){
        playSound(finId)
    }
}
