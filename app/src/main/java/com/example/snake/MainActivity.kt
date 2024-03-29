package com.example.snake

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.snake.databinding.ActivityMainBinding
import com.example.snakelogic.SnakeGameLogic

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var snakeGameLogic: SnakeGameLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        snakeGameLogic = SnakeGameLogic(this)

        snakeGameLogic.board = binding.board
        snakeGameLogic.relativeLayout = binding.relativeLayout
        snakeGameLogic.newGameButton = binding.newGame
        snakeGameLogic.tvTitle = binding.tvTitle

        binding.newGame.setOnClickListener {
            snakeGameLogic.startGame()
        }
    }
}
