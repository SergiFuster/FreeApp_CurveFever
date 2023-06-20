package com.example.freeapp_curvefever.Game.Controller

import com.example.freeapp_curvefever.Game.IView
import com.example.freeapp_curvefever.Game.Model.Model
import com.jcamenatuji.sharkuji.controller.GestureDetector
import es.uji.vj1229.framework.IGameController
import es.uji.vj1229.framework.TouchHandler
import es.uji.vj1229.framework.TouchHandler.TouchType.TOUCH_DOWN
import es.uji.vj1229.framework.TouchHandler.TouchType.TOUCH_UP
import es.uji.vj1229.framework.TouchHandler.TouchType.TOUCH_DRAGGED

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class Controller(private val view : IView, var model : Model?) : IGameController {
    private val gestureDetector = GestureDetector()
    var timer : Float = 0f
    var lastFrameBufferSave : Float = 0f
    var lastTimeInResultState : Float = timer
    var timeToStart : Float = 5f
    var timeInResults : Float = 5f

    override fun onUpdate(deltaTime: Float, touchEvents: MutableList<TouchHandler.TouchEvent>?) {
        timer += deltaTime
        if (touchEvents != null) for (event in touchEvents) {
            val correctedX = view.normalizeX(event.x)
            val correctedY = view.normalizeY(event.y)
            when (event.type) {
                TOUCH_DOWN -> gestureDetector.onTouchDown(model?.screenWidth!!, event.x, event.y)
                TOUCH_UP -> {
                    gestureDetector.onTouchUp()
                    if (model?.state == Model.State.FINISHED){
                        view.restartApplication()
                    }
                    else if(model?.state == Model.State.WAITING){
                        setInitialValues()
                        view.startMusic()
                        model?.startGame()
                    }
                }
                TOUCH_DRAGGED -> gestureDetector.onDragged(model?.screenWidth!!, event.x, event.y)
            }
        }

        if(model?.state == Model.State.WAITING) return
        updateState()
        if (model?.state == Model.State.RESULTS) return
        model?.rotations(deltaTime, gestureDetector.gesture)
        if (model?.state != Model.State.PLAYING) return
        model?.updateGame(deltaTime)
        model?.movement(deltaTime)
        model?.think(view.lastFrameBuffer, view.backGroundColor)
        if(view.lastFrameBuffer != null)
            model?.collisions(view.lastFrameBuffer!!, view.backGroundColor, view.soundEffects)

        view.updateAnimations(deltaTime)
    }

    private fun updateState(){
        when(model?.state){
            Model.State.STARTING -> if (timer >= timeToStart) {
                view.startNextRound()
                model?.state = Model.State.PLAYING
                view.soundEffects?.Start()
            }
            Model.State.RESULTS -> if (timer-lastTimeInResultState >= timeInResults){
                if (model?.game!!.rounds >= model?.game!!.maxRounds) {
                    model?.finishGame()
                }
                else {
                    setInitialValues()
                    view.startNextRound()
                    model?.startNextRound()
                }
            }
            Model.State.PLAYING -> if (model?.game!!.players.size - model?.game!!.roundRanking.size <= 1){
                model?.roundFinished()
                lastTimeInResultState = timer
            }
            else -> return
        }
    }

    fun saveFrameBufferIfMandatory(){
        if (timer-lastFrameBufferSave >= Model.TIME_TO_SAVE_FRAME_BUFFER) {
            lastFrameBufferSave = timer
            view.saveFrameBuffer()
        }
    }

    fun setInitialValues(){
        timer = 0f
        lastFrameBufferSave = 0f
        lastTimeInResultState = timer
        timeToStart = 5f
        timeInResults = 3f
    }
}