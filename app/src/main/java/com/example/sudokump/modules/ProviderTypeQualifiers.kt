package com.example.sudokump.modules

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EasySudokuGame

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MediumSudokuGame

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HardSudokuGame
