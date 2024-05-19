package com.android.post.data.database

import androidx.room.*

@Entity
data class ArticleEntity(
    @PrimaryKey val link: String,
    val title: String,
    val pubDate: String,
    val content: String
)

@Dao
interface ArticleDao {
    @Query("SELECT * FROM ArticleEntity")
    suspend fun getAll(): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg articles: ArticleEntity)
}

@Database(entities = [ArticleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
