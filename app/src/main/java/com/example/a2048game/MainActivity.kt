package com.example.a2048game


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.marginBottom
import androidx.room.Room


class MainActivity : AppCompatActivity() {

    private lateinit var gameLogic: GameLogic
    internal lateinit var tiles: Array<Array<TextView>>
    internal lateinit var tableRow: TableRow
    internal lateinit var linearLayout: LinearLayout
    internal lateinit var revertButton: Button
    internal lateinit var resetButton: Button

    internal lateinit var saveScoreLinearLayout: LinearLayout
    internal lateinit var saveScoreButton: Button

    internal lateinit var scoreTextView: TextView
    internal lateinit var topScoreTextView: TextView
    internal lateinit var nicknameEditText: EditText

    internal lateinit var scoreDao: ScoreDao

    private val n = 4
    val animationTime: Long = 1000
    var isAnimating = false

    var globalTopScore: Int = 0

    private val tileColorsMap = mapOf(
        0 to TileColor("#DFDFDF", "#DFDFDF"),
        2 to TileColor("#FDF9D7", "#000000"),
        4 to TileColor("#F8EB80", "#000000"),
        8 to TileColor("#FDE627", "#000000"),
        16 to TileColor("#FFD043", "#000000"),
        32 to TileColor("#FDAA2E", "#000000"),
        64 to TileColor("#FF612F", "#000000"),
        128 to TileColor("#E1F7FA", "#000000"),
        256 to TileColor("#81EDFB", "#000000"),
        512 to TileColor("#0FADF4", "#000000"),
        1024 to TileColor("#3F51B5", "#FDF9D7"),
        2048 to TileColor("#000000", "#FDF9D7")
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameLogic = GameLogic(n)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "scores"
        ).allowMainThreadQueries().build()
        scoreDao = db.scoreDao()

        val tilesTV1 = arrayOf(R.id.tile11, R.id.tile12, R.id.tile13, R.id.tile14).map { id -> findViewById<TextView>(id) }.toTypedArray()
        val tilesTV2 = arrayOf(R.id.tile21, R.id.tile22, R.id.tile23, R.id.tile24).map { id -> findViewById<TextView>(id) }.toTypedArray()
        val tilesTV3 = arrayOf(R.id.tile31, R.id.tile32, R.id.tile33, R.id.tile34).map { id -> findViewById<TextView>(id) }.toTypedArray()
        val tilesTV4 = arrayOf(R.id.tile41, R.id.tile42, R.id.tile43, R.id.tile44).map { id -> findViewById<TextView>(id) }.toTypedArray()
        tiles = arrayOf(tilesTV1, tilesTV2, tilesTV3, tilesTV4)

        tableRow = findViewById(R.id.tr1)
        linearLayout = findViewById(R.id.mainLinearLayout)
        revertButton = findViewById(R.id.revertButton)

        revertButton.isEnabled = false
        revertButton.isClickable = false

        resetButton = findViewById(R.id.resetButton)

        saveScoreButton = findViewById(R.id.saveScoreButton)
        nicknameEditText = findViewById(R.id.nicknameEditText)

        saveScoreLinearLayout = findViewById(R.id.saveScoreLinearLayout)
        saveScoreLinearLayout.visibility = View.INVISIBLE

        scoreTextView = findViewById(R.id.scoreTextView)
        scoreTextView.text = getString(R.string.score, gameLogic.score)

        topScoreTextView = findViewById(R.id.topScoreTextView)

        globalTopScore = scoreDao.getTopScorer().score!!
        topScoreTextView.text = getString(R.string.topScore, globalTopScore)

        revertButton.setOnClickListener{ onUndo() }
        resetButton.setOnClickListener{ onNewGame() }
        saveScoreButton.setOnClickListener{ onScoreSave() }

//        val margin = tableRow.marginBottom
//        val animationDistance = tiles[0][0].layoutParams.width.toFloat() + 2*margin

        gameLogic.startNewGame()
        updateTiles()

//        val oa = ObjectAnimator.ofFloat(tiles[0][0], "translationX", 2*animationDistance)
//        oa.apply {
//
//            duration = animationTime
//            isAnimating = true
//            start()
//        }
//        oa.doOnEnd { isAnimating = false }

        linearLayout.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                handleSwipe(Direction.LEFT)
                println("left")
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                println("right")
                handleSwipe(Direction.RIGHT)
            }
            override fun onSwipeUp() {
                super.onSwipeUp()
                println("up")
                handleSwipe(Direction.UP)
            }
            override fun onSwipeDown() {
                super.onSwipeDown()
                println("down")
                handleSwipe(Direction.DOWN)
            }
        })

//        while(!gameLogic.gameOver()){
//            handleSwipe(Direction.DOWN)
//            handleSwipe(Direction.LEFT)
//            handleSwipe(Direction.UP)
//            handleSwipe(Direction.RIGHT)
//        }
    }

    private fun onScoreSave() {
        val nicknameText = nicknameEditText.text.toString()
        scoreDao.insert(Score(nickname = (if (nicknameText != "") nicknameText else "guest"), score=gameLogic.score))
        saveScoreLinearLayout.visibility = View.INVISIBLE

    }

    private fun onNewGame() {
        gameLogic.startNewGame()
        updateTiles()
        scoreTextView.text = getString(R.string.score, gameLogic.score)

        revertButton.isEnabled = false
        revertButton.isClickable = false
    }

    private fun onUndo() {
        gameLogic.undo()
        scoreTextView.text = getString(R.string.score, gameLogic.score)
        updateTiles()

        revertButton.isEnabled = false
        revertButton.isClickable = false

        saveScoreLinearLayout.visibility = View.INVISIBLE

    }

    fun handleSwipe(direction: Direction){
        if (!isAnimating and !gameLogic.gameOver()){
            gameLogic.calculateMove(direction)
            updateTiles()

            scoreTextView.text = getString(R.string.score, gameLogic.score)
            if (gameLogic.score > globalTopScore)
            topScoreTextView.text = getString(R.string.topScore, gameLogic.score)

            revertButton.isEnabled = true
            revertButton.isClickable = true

            if (gameLogic.gameOver()) {
                println("Game over!")
                saveScoreLinearLayout.visibility = View.VISIBLE
            }
        }
    }

    fun updateTiles(){
        for (i in 0 until n) {
            for (j in 0 until n) {
                val tileValue = gameLogic.grid[i][j]
                tiles[i][j].text = tileValue.toString()
                val index: Int

                index = if (tileValue < 2048) tileValue else 2048
                tiles[i][j].setBackgroundColor(Color.parseColor(tileColorsMap[index]?.backgroundColor))
                tiles[i][j].setTextColor(Color.parseColor(tileColorsMap[index]?.textColor))
            }
        }
    }


}
