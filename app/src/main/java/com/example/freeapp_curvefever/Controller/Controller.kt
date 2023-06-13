package com.example.freeapp_curvefever.Controller

import com.example.freeapp_curvefever.IView
import com.example.freeapp_curvefever.Model.Model
import com.jcamenatuji.sharkuji.controller.GestureDetector
import es.uji.vj1229.framework.IGameController
import es.uji.vj1229.framework.TouchHandler
import es.uji.vj1229.framework.TouchHandler.TouchType.TOUCH_DOWN
import es.uji.vj1229.framework.TouchHandler.TouchType.TOUCH_UP
import es.uji.vj1229.framework.TouchHandler.TouchType.TOUCH_DRAGGED

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class Controller(private val view : IView, var model : Model?) : IGameController {
    private val gestureDetector = GestureDetector()
    override fun onUpdate(deltaTime: Float, touchEvents: MutableList<TouchHandler.TouchEvent>?) {
        if (touchEvents != null) for (event in touchEvents) {
            val correctedX = view.normalizeX(event.x)
            val correctedY = view.normalizeY(event.y)
            when (event.type) {
                TOUCH_DOWN -> gestureDetector.onTouchDown(model?.screenWidth!!, event.x, event.y)
                TOUCH_UP -> gestureDetector.onTouchUp()
                TOUCH_DRAGGED -> gestureDetector.onDragged(model?.screenWidth!!, event.x, event.y)
            }
        }

        model?.update(deltaTime, gestureDetector.gesture)

        if(view.lastFrameBuffer != null)
            model?.collisions(view.lastFrameBuffer!!, view.backGroundColor)
    }

    fun saveFrameBufferIfMandatory(){
        model?.saveFrameBufferIfMandatory { view.saveFrameBuffer() }
    }
}