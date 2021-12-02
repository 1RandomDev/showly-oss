package com.michaldrabik.data_local.database.dao

/* ktlint-disable */
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.michaldrabik.data_local.database.model.Movie
import com.michaldrabik.data_local.database.model.PersonCredits
import com.michaldrabik.data_local.database.model.Show

@Dao
interface PeopleCreditsDao : BaseDao<PersonCredits> {

  @Query(
    "SELECT shows.*, people_credits.created_at AS created_at, people_credits.updated_at AS updated_at FROM shows " +
      "INNER JOIN people_credits ON people_credits.id_trakt_show = shows.id_trakt WHERE people_credits.id_trakt_person = :personTraktId"
  )
  suspend fun getAllShowsForPerson(personTraktId: Long): List<Show>

  @Query(
    "SELECT movies.*, people_credits.created_at AS created_at, people_credits.updated_at AS updated_at FROM movies " +
      "INNER JOIN people_credits ON people_credits.id_trakt_show = movies.id_trakt WHERE people_credits.id_trakt_person = :personTraktId"
  )
  suspend fun getAllMoviesForPerson(personTraktId: Long): List<Movie>

  @Query("SELECT updated_at FROM people_credits WHERE id_trakt_person = :personTraktId LIMIT 1")
  suspend fun getTimestampForPerson(personTraktId: Long): Long?

  @Query("DELETE FROM people_credits WHERE id_trakt_person == :personTraktId")
  suspend fun deleteAllForPerson(personTraktId: Long)

  @Transaction
  suspend fun insert(personTraktId: Long, credits: List<PersonCredits>) {
    deleteAllForPerson(personTraktId)
    insert(credits)
  }
}
