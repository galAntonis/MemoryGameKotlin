package com.galeos.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galeos.mymemory.models.BoardSize
import com.galeos.mymemory.models.MemoryCard
import com.galeos.mymemory.models.MemoryGame
import com.galeos.mymemory.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }

    // View variables
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    // GAME DIFFICULTY
    private var boardSize: BoardSize = BoardSize.HARD


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()

        val memoryGame = MemoryGame(boardSize)
        setupRecyclerView(memoryGame.cards)
    }

    // Initialize the views
    private fun initializeViews(){
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
    }

    // Set the Recycler View adapter and layout
    private fun setupRecyclerView(memoryCard:List<MemoryCard>){
        rvBoard.adapter = MemoryBoardAdapter(this, boardSize,memoryCard,object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                Log.i(TAG,"Card clicked $position")
            }
        })
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
    }

}