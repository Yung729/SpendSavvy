package com.example.spendsavvy.data

import com.example.spendsavvy.R
import com.example.spendsavvy.models.Stock


val toyNames = listOf(
    "Blaze",
    "Buzzy",
    "Dazzle",
    "Fidget",
    "Fizzle",
    "Flare",
    "Flutter",
    "Gizmo",
    "Jumble",
    "Rumble",
    "Slinky",
    "Sparkle",
    "Squiggle",
    "Tinker",
    "Twisty",
    "Whirl",
    "Whiz",
    "Zigzag",
    "Zoomer"
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