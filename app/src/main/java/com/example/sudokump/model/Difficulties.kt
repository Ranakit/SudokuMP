package com.example.sudokump.model

enum class Difficulties {
    EASY, MEDIUM, HARD;

   override fun toString(): String {
        return when(this) {
            EASY -> "easy"
            MEDIUM -> "medium"
            HARD -> "hard"
        }
    }
}