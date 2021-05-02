package com.example.a2048game

interface GameLogicInterface {
    fun startNewGame()
    fun undo()
    fun calculateMove(direction: Direction)
    fun generateTile(): Boolean
    fun gameOver(): Boolean
}