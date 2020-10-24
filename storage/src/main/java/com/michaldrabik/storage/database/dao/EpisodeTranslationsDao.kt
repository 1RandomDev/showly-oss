package com.michaldrabik.storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michaldrabik.storage.database.model.EpisodeTranslation

@Dao
interface EpisodeTranslationsDao : BaseDao<EpisodeTranslation> {

  @Query("SELECT * FROM episodes_translations WHERE id_trakt == :traktEpisodeId AND id_trakt_show == :traktShowId AND language == :language")
  suspend fun getById(traktEpisodeId: Long, traktShowId: Long, language: String): EpisodeTranslation?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(translation: EpisodeTranslation)
}
