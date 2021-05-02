package com.example.a2048game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class HighScoresActivity : AppCompatActivity() {
    internal lateinit var scoresRecyclerView: RecyclerView
    internal lateinit var scoresAdapter: HighScoresAdapter
    internal lateinit var dataSet: Array<Score>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "scores"
        ).allowMainThreadQueries().build()
        val scoreDao = db.scoreDao()

        val dataSet: List<Score> = scoreDao.getAll()

        scoresRecyclerView = findViewById(R.id.scoresRecyclerView)
        scoresRecyclerView.layoutManager = LinearLayoutManager(this)
        scoresAdapter = HighScoresAdapter(emptyArray(), this)
        scoresRecyclerView.adapter = scoresAdapter

        scoresAdapter.dataSet = dataSet.toTypedArray()
        scoresAdapter.notifyDataSetChanged()

    }
}