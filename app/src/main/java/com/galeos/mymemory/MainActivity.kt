package com.galeos.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galeos.mymemory.models.BoardSize
import com.galeos.mymemory.models.MemoryCard
import com.galeos.mymemory.models.MemoryGame

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var adapter: MemoryBoardAdapter
    private lateinit var memoryGame: MemoryGame

    // View variables
    private lateinit var rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var tvNumPairs: TextView

    // GAME DIFFICULTY
    private var boardSize: BoardSize = BoardSize.EASY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()

        memoryGame = MemoryGame(boardSize)
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
        Log.i(TAG,"$memoryCard")
        adapter = MemoryBoardAdapter(this, boardSize,memoryCard,object: MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                updateGameWithFlip(position)
            }
        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
    }

    private fun updateGameWithFlip(position: Int) {
        //Error Checking
        if(memoryGame.haveWonGame()){
            Toast.makeText(this,"You already won!", Toast.LENGTH_SHORT).show()
            return
        }
        if(memoryGame.isCardFaceUp(position)){
            Toast.makeText(this,"Invalid move!", Toast.LENGTH_SHORT).show()
            return
        }
        if(memoryGame.flipCard(position)){
            Log.i(TAG,"Found a match! Num pairs found: ${memoryGame.numPairsFound}")
            tvNumPairs.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"

            if (memoryGame.haveWonGame()){
                Toast.makeText(this,"You won! Congratulations!", Toast.LENGTH_SHORT).show()
            }
        }

        tvNumMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        adapter.notifyDataSetChanged()
    }

}