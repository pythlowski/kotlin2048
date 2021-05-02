package com.example.a2048game

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HighScoresAdapter(var dataSet: Array<Score>, private var context: Context): RecyclerView.Adapter<HighScoresAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val positionTextView: TextView
        val nicknameTextView: TextView
        val scoreValueTextView: TextView

        init {
            positionTextView = view.findViewById(R.id.positionTextView)
            nicknameTextView = view.findViewById(R.id.nicknameTextView)
            scoreValueTextView = view.findViewById(R.id.scoreValueTextView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.score_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val scoreInfo = dataSet[position]
        if (scoreInfo.position == 0) scoreInfo.position = position + 1

        viewHolder.positionTextView.text = scoreInfo.position.toString()
        viewHolder.nicknameTextView.text = if (scoreInfo.nickname!!.length < 16) scoreInfo.nickname else scoreInfo.nickname.take(15).plus("...")
        viewHolder.scoreValueTextView.text = scoreInfo.score.toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}