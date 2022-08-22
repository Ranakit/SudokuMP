package com.example.sudokump.persistency.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_games")
data class SavedGameDBEntity(@PrimaryKey(autoGenerate = true) val id: Int,
                             @ColumnInfo(name = "seconds_passed") var secondsPassed : Int,
                             @ColumnInfo(name = "completion_percent") var completionPercent : String,
                             @ColumnInfo(name = "saved_schema") var savedSchema : ByteArray,
                             @ColumnInfo(name = "difficulty") var difficulty : String,
                             @ColumnInfo(name = "save_date") var savedDate : String
                             ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SavedGameDBEntity

        if (id != other.id) return false
        if (secondsPassed != other.secondsPassed) return false
        if (completionPercent != other.completionPercent) return false
        if (!savedSchema.contentEquals(other.savedSchema)) return false
        if (difficulty != other.difficulty) return false
        if (savedDate != other.savedDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + secondsPassed
        result = 31 * result + completionPercent.hashCode()
        result = 31 * result + savedSchema.contentHashCode()
        result = 31 * result + difficulty.hashCode()
        result = 31 * result + savedDate.hashCode()
        return result
    }

}