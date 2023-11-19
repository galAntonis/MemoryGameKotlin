package com.galeos.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.galeos.mymemory.models.BoardSize
import com.galeos.mymemory.models.MemoryCard
import com.galeos.mymemory.models.MemoryGame
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener

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
        setupBoard()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_refresh -> {
                if(memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?",null,View.OnClickListener {
                        setupBoard()
                    })
                }else{
                    setupBoard()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(title:String,view: View?, positiveClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel",null)
            .setPositiveButton("OK") {
                _,_ -> positiveClickListener.onClick(null)
            }.show()
    }

    private fun setupBoard(){
        memoryGame = MemoryGame(boardSize)
        setupRecyclerView(memoryGame.cards)

        when(boardSize) {
            BoardSize.EASY-> {
                tvNumMoves.text = "Easy: 4x2"
                tvNumPairs.text = "Pairs: 0 / 4"
            }
            BoardSize.MEDIUM-> {
                tvNumMoves.text = "Medium: 6x3"
                tvNumPairs.text = "Pairs: 0 / 9"
            }
            BoardSize.HARD-> {
                tvNumMoves.text = "Hard: 6x4"
                tvNumPairs.text = "Pairs: 0 / 12"
            }
        }
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