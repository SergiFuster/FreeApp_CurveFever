package com.jcamenatuji.sharkuji.controller

class GestureDetector {
    enum class Gestures {
        RIGHT, LEFT
    }

    var gesture : Gestures? = null

    fun onTouchDown(screenWidth : Int, x: Int, y: Int) {
        gesture = if(x < screenWidth / 2) Gestures.LEFT
        else Gestures.RIGHT
    }

    fun onTouchUp(){
        gesture = null
    }

    fun onDragged(screenWidth: Int, x: Int, y: Int) {
        gesture = if(x < screenWidth / 2) Gestures.LEFT
        else Gestures.RIGHT
    }
}