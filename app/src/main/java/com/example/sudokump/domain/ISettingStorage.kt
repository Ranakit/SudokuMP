package com.fabrizio.sudoku.domain

interface ISettingStorage {

    suspend fun getSettings(): SettingsStorageResult
    suspend fun updateSettings(settings: Settings): SettingsStorageResult


}



sealed class SettingsStorageResult{

    data class OnSuccess(val settings: Settings) : SettingsStorageResult()
    data class OnErrore(val exception : Exception): SettingsStorageResult()

}