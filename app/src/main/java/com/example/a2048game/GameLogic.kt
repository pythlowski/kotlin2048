package com.example.a2048game

class GameLogic(val n: Int): GameLogicInterface {
    var grid = Array(n) {Array(n) {0} }
    var history = Array(n) {Array(n) {0} }
//    var animation = Array(n) {Array(n) {0} }
    var score: Int = 0
    var scoreRound: Int = 0
    var scoreHistory: Int = 0

    override fun startNewGame() {
        grid = Array(n) {Array(n) {0} }
        history = Array(n) {Array(n) {0} }
//        animation = Array(n) {Array(n) {0} }
        score= 0
        scoreRound= 0
        scoreHistory = 0
        for (i in 0 until n-2) {
            generateTile()
        }
    }

    override fun calculateMove(direction: Direction) {
        for(i in 0 until n){
            for(j in 0 until n){
                history[i][j] = grid[i][j]
            }
        }
        scoreRound = 0

        for (i in 0 until n-2) {
            push(direction)
        }
        calc(direction)
        push(direction)

        score += scoreRound

        generateTile()
    }

    private fun push(direction: Direction){
        when(direction) {
            Direction.RIGHT -> {
                for (i in 0 until n) {
                    for (j in n-1 downTo 1){
                        if((grid[i][j] == 0) and (grid[i][j-1] != 0)) {
                            grid[i][j] = grid[i][j-1]
                            grid[i][j-1] = 0
                        }
                    }
                }
            }
            Direction.LEFT -> {
                for (i in 0 until n) {
                    for (j in 0 until n-1){
                        if((grid[i][j] == 0) and (grid[i][j+1] != 0)) {
                            grid[i][j] = grid[i][j+1]
                            grid[i][j+1] = 0
                        }
                    }
                }
            }
            Direction.UP -> {
                for (j in 0 until n) {
                    for (i in 0 until n-1){
                        if((grid[i][j] == 0) and (grid[i+1][j] != 0)) {
                            grid[i][j] = grid[i+1][j]
                            grid[i+1][j] = 0
                        }
                    }
                }
            }
            Direction.DOWN -> {
                for (j in 0 until n) {
                    for (i in n-1 downTo 1){
                        if((grid[i][j] == 0) and (grid[i-1][j] != 0)) {
                            grid[i][j] = grid[i-1][j]
                            grid[i-1][j] = 0
                        }
                    }
                }
            }
        }
    }

    private fun calc(direction: Direction){
        when(direction) {
            Direction.RIGHT -> {
                for (i in 0 until n) {
                    for (j in n-1 downTo 1) {
                        if (grid[i][j] == grid[i][j-1]) {
                            grid[i][j-1] = 0
                            val result = grid[i][j] * 2
                            grid[i][j] = result
                            scoreRound += result
                        }
                    }
                }
            }
            Direction.LEFT -> {
                for (i in 0 until n) {
                    for (j in 0 until n-1) {
                        if (grid[i][j] == grid[i][j+1]) {
                            grid[i][j+1] = 0
                            val result = grid[i][j] * 2
                            grid[i][j] = result
                            scoreRound += result
                        }
                    }
                }
            }
            Direction.UP -> {
                for (j in 0 until n) {
                    for (i in 0 until n-1) {
                        if (grid[i][j] == grid[i+1][j]) {
                            grid[i+1][j] = 0
                            val result = grid[i][j] * 2
                            grid[i][j] = result
                            scoreRound += result
                        }
                    }
                }
            }

            Direction.DOWN -> {
                for (j in 0 until n) {
                    for (i in n-1 downTo 1) {
                        if (grid[i][j] == grid[i-1][j]) {
                            grid[i-1][j] = 0
                            val result = grid[i][j] * 2
                            grid[i][j] = result
                            scoreRound += result
                        }
                    }
                }
            }
        }
    }

    override fun undo() {
        for(i in 0 until n){
            for(j in 0 until n){
                grid[i][j] = history[i][j]
            }
        }
        score -= scoreRound
        scoreRound = 0
    }

    override fun gameOver(): Boolean{
        if (grid.all { row -> !row.contains(0) }){
            var gameOver = true

            // check joinable tiles horizontally
            for(i in 0 until n){
                for(j in 0 until n-1){
                    if (grid[i][j] == grid[i][j+1]) gameOver = false
                }
            }
            // check joinable tiles vertically
            for(j in 0 until n){
                for(i in 0 until n-1){
                    if (grid[i][j] == grid[i+1][j]) gameOver = false
                }
            }
            return gameOver
        }
        return false
    }

    override fun generateTile(): Boolean {
        var candidates = mutableListOf<Pair<Int, Int>>()
        var generatable = false

        for (i in 0 until n) {
            for (j in 0 until n) {
                if (grid[i][j] == 0) {
                    candidates.add(Pair(i, j))
                    generatable = true
                }
            }
        }
        if (generatable) {
            val choice = candidates.random()
            grid[choice.first][choice.second] = if (Math.random() > 0.10) 2 else 4
        }
        return generatable
    }
}



