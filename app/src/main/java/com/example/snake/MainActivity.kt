package com.example.snake

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.snake.databinding.ActivityMainBinding
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : Activity(), GestureDetector.OnGestureListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var gestureDetector: GestureDetector
    private var currentDirection = "right"
    private var isGameRunning = false
    private val snakeSegments = mutableListOf<ImageView>()
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gestureDetector = GestureDetector(this, this)

        binding.board.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }

        binding.newGame.setOnClickListener {
            startNewGame()
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


    private fun startNewGame() {
        if (isGameRunning) return

        isGameRunning = true
        binding.board.visibility = View.VISIBLE
        binding.relativeLayout.visibility = View.VISIBLE
        binding.newGame.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE

        for (segment in snakeSegments) {
            binding.board.removeView(segment)
        }
        snakeSegments.clear()

        val snake = ImageView(this)
        val meat = ImageView(this)

        binding.board.findViewWithTag<ImageView>("meat")?.let {
            binding.board.removeView(it)
        }

        snake.setImageResource(R.drawable.snake)
        snake.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.board.addView(snake)
        snakeSegments.add(snake)

        var snakeX = snake.x
        var snakeY = snake.y

        meat.setImageResource(R.drawable.meat)
        meat.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        meat.tag = "meat"
        binding.board.addView(meat)

        val random = Random()
        val randomX = random.nextInt(801) - 300
        val randomY = random.nextInt(801) - 300

        meat.x = randomX.toFloat()
        meat.y = randomY.toFloat()

        var delayMillis = 30L

        val runnable = object : Runnable {
            override fun run() {
                for (i in snakeSegments.size - 1 downTo 1) {
                    snakeSegments[i].x = snakeSegments[i - 1].x
                    snakeSegments[i].y = snakeSegments[i - 1].y
                }

                when (currentDirection) {
                    "up" -> {
                        snakeY -= 10
                        if (snakeY < -490) {
                            snakeY = (binding.board.height / 2 - snake.height + 30).toFloat()
                        }
                        snake.translationY = snakeY
                    }

                    "down" -> {
                        snakeY += 10
                        val maxY = binding.board.height / 2 - snake.height + 30
                        if (snakeY > maxY) {
                            snakeY = -490f
                        }
                        snake.translationY = snakeY
                    }

                    "left" -> {
                        snakeX -= 10
                        if (snakeX < -490) {
                            snakeX = (binding.board.width / 2 - snake.width + 30).toFloat()
                        }
                        snake.translationX = snakeX
                    }

                    "right" -> {
                        snakeX += 10
                        val maxX = binding.board.width / 2 - snake.width + 30
                        if (snakeX > maxX) {
                            snakeX = -490f
                        }
                        snake.translationX = snakeX
                    }
                }
                checkFoodCollision()
                handler.postDelayed(this, delayMillis)
                checkSelfCollision()
            }
        }
        handler.postDelayed(runnable, delayMillis)
    }

    private fun endGame() {
        isGameRunning = false
        handler.removeCallbacksAndMessages(null)
        binding.board.visibility = View.GONE
        binding.relativeLayout.visibility = View.GONE
        binding.relativeLayout.setBackgroundColor(resources.getColor(R.color.red))
        binding.tvTitle.visibility = View.VISIBLE
        binding.newGame.visibility = View.VISIBLE
    }

    private fun checkFoodCollision() {
        val snake = snakeSegments[0]
        val meat = binding.board.findViewWithTag<ImageView>("meat")

        val distanceThreshold = 50
        val distance = sqrt((snake.x - meat.x).pow(2) + (snake.y - meat.y).pow(2))

        if (distance < distanceThreshold) {
            val newSnake = ImageView(this)
            newSnake.setImageResource(R.drawable.snake)
            newSnake.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            binding.board.addView(newSnake)
            snakeSegments.add(newSnake)
            val random = Random()
            val maxX = (binding.board.width - 100) - meat.width
            val maxY = (binding.board.height - 100) - meat.height
            val randomX = random.nextInt(maxX).coerceAtLeast(0)
            val randomY = random.nextInt(maxY).coerceAtLeast(0)
            meat.x = randomX.toFloat()
            meat.y = randomY.toFloat()
        }
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {}

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent) {}

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val dx = e2.x - (e1?.x ?: 0f)
        val dy = e2.y - (e1?.y ?: 0f)

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                currentDirection = "right"
            } else {
                currentDirection = "left"
            }
        } else {
            if (dy > 0) {
                currentDirection = "down"
            } else {
                currentDirection = "up"
            }
        }
        return true
    }
}