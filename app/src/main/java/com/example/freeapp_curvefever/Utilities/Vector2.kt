package com.example.freeapp_curvefever.Utilities
import android.util.Log
import kotlin.math.*

open class Vector2(var x: Double, var y: Double) {

    constructor(x : Int, y : Int) : this(x.toDouble(), y.toDouble())

    fun magnitude(): Double {
        return sqrt(x * x + y * y)
    }

    private fun directionTo(other: Vector2): Vector2 {
        val dx = other.x - x
        val dy = other.y - y
        return Vector2(dx, dy)
    }

    fun copyInto(other : Vector2){
        other.x = this.x
        other.y = this.y
    }

    fun normalize() {
        val mag = magnitude()
        x /= mag
        y /= mag
    }

    fun rotate(degrees: Float, right: Boolean = true) {
        val radians = Math.toRadians(degrees.toDouble())
        val cos = cos(radians)
        val sin = sin(radians)

        val direction = if (right) -1 else 1

        val newX = x * cos + direction * y * sin
        val newY = -direction * x * sin + y * cos

        x = newX
        y = newY
    }


    fun distanceTo(other: Vector2): Double {
        val direction = directionTo(other)
        return direction.magnitude()
    }

    fun right() : Vector2{
        return Vector2(-y, x)
    }

    fun left() : Vector2{
        return Vector2(y, -x)
    }
    fun normalized(): Vector2 {
        val mag = magnitude()
        return Vector2(x / mag, y / mag)
    }

    fun copy() : Vector2{
        return Vector2(x, y)
    }

    fun setLength(length : Float){
        val newVector : Vector2 = normalized() * length
        x = newVector.x
        y = newVector.y
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other !is Vector2) return false
        return x == other.x && y == other.y
    }

    override fun toString(): String {
        return "($x, $y)"
    }
    operator fun plus(other: Vector2) : Vector2{
        return Vector2(this.x + other.x, this.y+other.y)
    }

    operator fun times(scalar : Float) : Vector2{
        return Vector2(x * scalar, y * scalar)
    }

    operator fun times(vector : Vector2) : Vector2{
        return Vector2(x * vector.x, y * vector.y)
    }

    operator fun times(scalar : Int) : Vector2{
        return Vector2(x * scalar, y * scalar)
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }


    companion object{
        val zero : Vector2
            get() =  Vector2(0.0, 0.0)

        val right: Vector2
            get() = Vector2(1.0, 0.0)

        val left: Vector2
            get() = Vector2(-1.0, 0.0)

        val up: Vector2
            get() = Vector2(0.0, -1.0)

        val down: Vector2
            get() = Vector2(0.0, 1.0)
    }

}
