package com.example.sudokump.modules;

import com.example.sudokump.model.SudokuGrid;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SudokuAPI {
    @GET("board")
    Call <SudokuGrid> sudokuGrid (@Query("difficulty") String difficulty);
}
