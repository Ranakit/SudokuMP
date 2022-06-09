package com.fabrizio.sudoku.domain
/*
in this class we can implement the handlers for the user's statistics that will bene generated
after each game
 */
data class UserStatistics(
    val fourEasy: Long = 0,
    val fourMedium: Long =  0,
    val fourHard: Long = 0,
    val nineEasy: Long = 0,
    val nineMedium: Long = 0,
    val nineHard: Long = 0,

)
