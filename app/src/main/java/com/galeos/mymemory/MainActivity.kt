package com.galeos.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galeos.mymemory.models.BoardSize
import com.galeos.mymemory.models.MemoryCard
import com.galeos.mymemory.utils.DEFAULT_ICONS

class MainActivity : AppCompatActivity() {

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
        val randomizedImages = generateRandomizedImages()
        val memoryCard:List<MemoryCard> = randomizedImages.map{MemoryCard(it)}
        setupRecyclerView(memoryCard)
    }

    // Initialize the views
    private fun initializeViews(){
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
    }

    // Set the Recycler View adapter and layout
    private fun setupRecyclerView(memoryCard:List<MemoryCard>){
        rvBoard.adapter = MemoryBoardAdapter(this, boardSize,memoryCard)
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager(this,boardSize.getWidth())
    }

    // Choose Random Image pairs
    private fun generateRandomizedImages(): List<Int> {
        val chosenImages: List<Int> = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        return (chosenImages + chosenImages).shuffled()
    }
}