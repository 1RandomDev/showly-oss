package com.michaldrabik.ui_my_shows.watchlist.cases

import com.michaldrabik.common.di.AppScope
import com.michaldrabik.ui_my_shows.watchlist.recycler.WatchlistListItem
import com.michaldrabik.ui_repository.RatingsRepository
import com.michaldrabik.ui_repository.UserTraktManager
import javax.inject.Inject

@AppScope
class WatchlistRatingsCase @Inject constructor(
  private val ratingsRepository: RatingsRepository,
  private val userTraktManager: UserTraktManager
) {

  suspend fun loadRatings(items: List<WatchlistListItem>): List<WatchlistListItem> {
    if (!userTraktManager.isAuthorized()) return items

    val token = userTraktManager.checkAuthorization().token
    ratingsRepository.preloadShowsRatings(token)

    return items.map {
      val rating = ratingsRepository.loadRating(token, it.show)
      it.copy(userRating = rating?.rating)
    }
  }
}
