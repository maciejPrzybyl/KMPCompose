import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbNote(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String
)