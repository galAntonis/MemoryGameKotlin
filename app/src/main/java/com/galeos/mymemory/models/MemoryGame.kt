package com.galeos.mymemory.models

import com.galeos.mymemory.utils.DEFAULT_ICONS

class MemoryGame (private val boardSize: BoardSize){

    val cards : List<MemoryCard>
    var numPairsFound = 0

    private var indexOfSingleSelectedCard: Int? = null

    init {
        val chosenImages: List<Int> = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map{MemoryCard(it)}
    }

    fun flipCard(position: Int): Boolean {
        val card: MemoryCard = cards[position]
        var foundMatch = false;
        if(indexOfSingleSelectedCard == null){
            // 0 or 2
            restoreCards()
            indexOfSingleSelectedCard = position
        }else{
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!,position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if(cards[position1].identifier!=cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound += 1
        return true
    }

    private fun restoreCards() {
        for (card:MemoryCard in cards){
            if(!card.isMatched){
                card.isFaceUp = false
            }
        }
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }
}