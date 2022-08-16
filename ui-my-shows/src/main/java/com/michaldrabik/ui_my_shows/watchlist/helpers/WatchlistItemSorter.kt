package com.michaldrabik.ui_my_shows.watchlist.helpers

import com.michaldrabik.ui_model.SortOrder
import com.michaldrabik.ui_model.SortOrder.DATE_ADDED
import com.michaldrabik.ui_model.SortOrder.NAME
import com.michaldrabik.ui_model.SortOrder.NEWEST
import com.michaldrabik.ui_model.SortOrder.RATING
import com.michaldrabik.ui_model.SortOrder.USER_RATING
import com.michaldrabik.ui_model.SortType
import com.michaldrabik.ui_model.SortType.ASCENDING
import com.michaldrabik.ui_model.SortType.DESCENDING
import com.michaldrabik.ui_my_shows.watchlist.recycler.WatchlistListItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistItemSorter @Inject constructor() {

  fun sort(sortOrder: SortOrder, sortType: SortType) = when (sortType) {
    ASCENDING -> sortAscending(sortOrder)
    DESCENDING -> sortDescending(sortOrder)
  }

  private fun sortAscending(sortOrder: SortOrder) = when (sortOrder) {
    NAME -> compareBy { getTitle(it) }
    RATING -> compareBy { it.show.rating }
    USER_RATING ->
      compareByDescending<WatchlistListItem> { it.userRating != null }
        .thenBy { it.userRating }
        .thenBy { getTitle(it) }
    DATE_ADDED -> compareBy { it.show.createdAt }
    NEWEST -> compareBy<WatchlistListItem> { it.show.firstAired }.thenBy { it.show.year }
    else -> throw IllegalStateException("Invalid sort order")
  }

  private fun sortDescending(sortOrder: SortOrder) = when (sortOrder) {
    NAME -> compareByDescending { getTitle(it) }
    RATING -> compareByDescending { it.show.rating }
    USER_RATING ->
      compareByDescending<WatchlistListItem> { it.userRating != null }
        .thenByDescending { it.userRating }
        .thenBy { getTitle(it) }
    DATE_ADDED -> compareByDescending { it.show.createdAt }
    NEWEST -> compareByDescending<WatchlistListItem> { it.show.firstAired }.thenByDescending { it.show.year }
    else -> throw IllegalStateException("Invalid sort order")
  }

  private fun getTitle(item: WatchlistListItem): String {
    val translatedTitle =
      if (item.translation?.hasTitle == true) item.translation.title
      else item.show.titleNoThe
    return translatedTitle.uppercase()
  }
}
