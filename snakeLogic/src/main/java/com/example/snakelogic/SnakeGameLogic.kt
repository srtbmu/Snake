//// SnakeGameLogic.kt
//
//package com.example.snakelogic
//
//import android.content.Context
//import android.os.Handler
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import java.util.Random
//import kotlin.math.pow
//import kotlin.math.sqrt
//
//class SnakeGameLogic(private val context: Context) : View.OnTouchListener {
//    lateinit var board: ViewGroup
//    lateinit var relativeLayout: ViewGroup
//    lateinit var newGameButton: View
//    lateinit var tvTitle: View
//
//    private var currentDirection = "right"
//    private var isGameRunning = false
//    private val snakeSegments = mutableListOf<ImageView>()
//    private val handler = Handler()
//
//
//    fun startGame() {
//        isGameRunning = true
//        board.visibility = View.VISIBLE
//        relativeLayout.visibility = View.VISIBLE
//        newGameButton.visibility = View.GONE
//        tvTitle.visibility = View.GONE
//
//        // Initialize the snake
//        val snake = ImageView(context)
//        snake.setImageResource(R.drawable.snake)
//        snake.layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        board.addView(snake)
//        snakeSegments.add(snake)
//
//        var snakeX = snake.x
//        var snakeY = snake.y
//
//        // Start the game loop
//        handler.postDelayed(gameLoopRunnable, 30L)
//    }
//
//    private val gameLoopRunnable = object : Runnable {
//        override fun run() {
//            // Move the snake
//            moveSnake()
//
//            // Check for collisions
//            checkFoodCollision()
//            checkSelfCollision()
//
//            // Continue the game loop
//            handler.postDelayed(this, 30L)
//        }
//    }
//
//    private fun moveSnake() {
//        val snake = snakeSegments[0]
//        var snakeX = snake.x
//        var snakeY = snake.y
//
//        for (i in snakeSegments.size - 1 downTo 1) {
//            snakeSegments[i].x = snakeSegments[i - 1].x
//            snakeSegments[i].y = snakeSegments[i - 1].y
//        }
//
//        when (currentDirection) {
//            "up" -> {
//                snakeY -= 10
//                if (snakeY < -490) {
//                    snakeY = (board.height / 2 - snake.height + 30).toFloat()
//                }
//                snake.translationY = snakeY
//            }
//
//            "down" -> {
//                snakeY += 10
//                val maxY = board.height / 2 - snake.height + 30
//                if (snakeY > maxY) {
//                    snakeY = -490f
//                }
//                snake.translationY = snakeY
//            }
//
//            "left" -> {
//                snakeX -= 10
//                if (snakeX < -490) {
//                    snakeX = (board.width / 2 - snake.width + 30).toFloat()
//                }
//                snake.translationX = snakeX
//            }
//
//            "right" -> {
//                snakeX += 10
//                val maxX = board.width / 2 - snake.width + 30
//                if (snakeX > maxX) {
//                    snakeX = -490f
//                }
//                snake.translationX = snakeX
//            }
//        }
//    }
//
//    private fun checkFoodCollision() {
//        val snake = snakeSegments[0]
//        val meat = board.findViewWithTag<ImageView>("meat")
//
//        if (meat != null) {
//            val distanceThreshold = 50
//            val distance =
//                sqrt((snake.x - meat.x).pow(2) + (snake.y - meat.y).pow(2))
//
//            if (distance < distanceThreshold) {
//                val newSnake = ImageView(context)
//                newSnake.setImageResource(R.drawable.snake)
//                newSnake.layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//                )
//                board.addView(newSnake)
//                snakeSegments.add(newSnake)
//                val random = Random()
//                val maxX = (board.width - 100) - meat.width
//                val maxY = (board.height - 100) - meat.height
//                val randomX = random.nextInt(maxX).coerceAtLeast(0)
//                val randomY = random.nextInt(maxY).coerceAtLeast(0)
//                meat.x = randomX.toFloat()
//                meat.y = randomY.toFloat()
//            }
//        }
//    }
//
//
//    private fun checkSelfCollision() {
//        val head = snakeSegments[0]
//        for (i in 1 until snakeSegments.size) {
//            val segment = snakeSegments[i]
//            if (head.x == segment.x && head.y == segment.y) {
//                endGame()
//                break
//            }
//        }
//    }
//
//    private fun endGame() {
//        isGameRunning = false
//        handler.removeCallbacksAndMessages(null)
//        board.visibility = View.GONE
//        relativeLayout.visibility = View.GONE
//        relativeLayout.setBackgroundColor(context.resources.getColor(R.color.red))
//        tvTitle.visibility = View.VISIBLE
//        newGameButton.visibility = View.VISIBLE
//    }
//
//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                // Handle touch down event
//            }
//            MotionEvent.ACTION_MOVE -> {
//                // Handle touch move event
//            }
//            MotionEvent.ACTION_UP -> {
//                // Handle touch up event
//                handleSwipe(event)
//            }
//        }
//        return true
//    }
//
//    private fun handleSwipe(event: MotionEvent) {
//        val dx = event.x - event.rawX
//        val dy = event.y - event.rawY
//
//        if (Math.abs(dx) > Math.abs(dy)) {
//            if (dx > 0) {
//                // Swipe right
//                currentDirection = "right"
//            } else {
//                // Swipe left
//                currentDirection = "left"
//            }
//        } else {
//            if (dy > 0) {
//                // Swipe down
//                currentDirection = "down"
//            } else {
//                // Swipe up
//                currentDirection = "up"
//            }
//        }
//    }
//
//}
// SnakeGameLogic.kt

package com.example.snakelogic

import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class SnakeGameLogic(private val context: Context) : View.OnTouchListener {
    lateinit var board: ViewGroup
    lateinit var relativeLayout: ViewGroup
    lateinit var newGameButton: View
    lateinit var tvTitle: View

    private var currentDirection = "right"
    private var isGameRunning = false
    private val snakeSegments = mutableListOf<ImageView>()
    private val handler = Handler()


    fun startGame() {
        isGameRunning = true
        board.visibility = View.VISIBLE
        relativeLayout.visibility = View.VISIBLE
        newGameButton.visibility = View.GONE
        tvTitle.visibility = View.GONE

        // Initialize the snake
        val snake = ImageView(context)
        snake.setImageResource(R.drawable.snake)
        snake.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        board.addView(snake)
        snakeSegments.add(snake)

        var snakeX = snake.x
        var snakeY = snake.y

        // Start the game loop
        handler.postDelayed(gameLoopRunnable, 30L)
    }

    private val gameLoopRunnable = object : Runnable {
        override fun run() {
            // Move the snake
            moveSnake()

            // Check for collisions
            checkFoodCollision()
            checkSelfCollision()

            // Continue the game loop
            handler.postDelayed(this, 30L)
        }
    }

    private fun moveSnake() {
        val snake = snakeSegments[0]
        var snakeX = snake.x
        var snakeY = snake.y

        for (i in snakeSegments.size - 1 downTo 1) {
            snakeSegments[i].x = snakeSegments[i - 1].x
            snakeSegments[i].y = snakeSegments[i - 1].y
        }

        when (currentDirection) {
            "up" -> {
                snakeY -= 10
                if (snakeY < -490) {
                    snakeY = (board.height / 2 - snake.height + 30).toFloat()
                }
                snake.translationY = snakeY
            }

            "down" -> {
                snakeY += 10
                val maxY = board.height / 2 - snake.height + 30
                if (snakeY > maxY) {
                    snakeY = -490f
                }
                snake.translationY = snakeY
            }

            "left" -> {
                snakeX -= 10
                if (snakeX < -490) {
                    snakeX = (board.width / 2 - snake.width + 30).toFloat()
                }
                snake.translationX = snakeX
            }

            "right" -> {
                snakeX += 10
                val maxX = board.width / 2 - snake.width + 30
                if (snakeX > maxX) {
                    snakeX = -490f
                }
                snake.translationX = snakeX
            }
        }
    }

    private fun checkFoodCollision() {
        val snake = snakeSegments[0]
        val meat = board.findViewWithTag<ImageView>("meat")

        if (meat != null) {
            val distanceThreshold = 50
            val distance =
                sqrt((snake.x - meat.x).pow(2) + (snake.y - meat.y).pow(2))

            if (distance < distanceThreshold) {
                val newSnake = ImageView(context)
                newSnake.setImageResource(R.drawable.snake)
                newSnake.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                board.addView(newSnake)
                snakeSegments.add(newSnake)
                val random = Random()
                val maxX = (board.width - 100) - meat.width
                val maxY = (board.height - 100) - meat.height
                val randomX = random.nextInt(maxX).coerceAtLeast(0)
                val randomY = random.nextInt(maxY).coerceAtLeast(0)
                meat.x = randomX.toFloat()
                meat.y = randomY.toFloat()
            }
        }
    }

    private fun checkSelfCollision() {
        val head = snakeSegments[0]
        for (i in 1 until snakeSegments.size) {
            val segment = snakeSegments[i]
            if (head.x == segment.x && head.y == segment.y) {
                endGame()
                break
            }
        }
    }

    private fun endGame() {
        isGameRunning = false
        handler.removeCallbacksAndMessages(null)
        board.visibility = View.GONE
        relativeLayout.visibility = View.GONE
        relativeLayout.setBackgroundColor(context.resources.getColor(R.color.red))
        tvTitle.visibility = View.VISIBLE
        newGameButton.visibility = View.VISIBLE
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // Handle touch down event
            }
            MotionEvent.ACTION_MOVE -> {
                // Handle touch move event
            }
            MotionEvent.ACTION_UP -> {
                // Handle touch up event
                handleSwipe(event)
            }
        }
        return true
    }

    private fun handleSwipe(event: MotionEvent) {
        val dx = event.x - event.rawX
        val dy = event.y - event.rawY

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                // Swipe right
                currentDirection = "right"
            } else {
                // Swipe left
                currentDirection = "left"
            }
        } else {
            if (dy > 0) {
                // Swipe down
                currentDirection = "down"
            } else {
                // Swipe up
                currentDirection = "up"
            }
        }
    }

}
