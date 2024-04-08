package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.State.Stock


val toyNames = listOf(
    "Slinky",
    "Buzzy",
    "Twisty",
    "Gizmo",
    "Zoomer",
    "Flutter",
    "Rumble",
    "Whirl",
    "Blaze",
    "Dazzle",
    "Zigzag",
    "Squiggle",
    "Fizzle",
    "Tinker",
    "Flare",
    "Jumble",
    "Sparkle",
    "Whiz",
    "Fidget",
)

fun getRandomToyName(): String {
    return toyNames.random()
}
class StockData {

    fun loadStock() : List<Stock>{
        return listOf<Stock>(
            Stock(R.drawable.wallet,getRandomToyName(),30,50.0),
            Stock(R.drawable.wallet,"Daemon",10,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),30,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0),
            Stock(R.drawable.wallet,getRandomToyName(),50,50.0)

        )
    }
}