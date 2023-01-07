package com.michaldrabik.ui_my_movies.watchlist

import com.michaldrabik.ui_base.utilities.events.Event
import com.michaldrabik.ui_model.SortOrder
import com.michaldrabik.ui_model.SortType
import com.michaldrabik.ui_my_movies.common.recycler.CollectionListItem

data class WatchlistUiState(
  val items: List<CollectionListItem> = emptyList(),
  val resetScroll: Event<Boolean>? = null,
  val sortOrder: Event<Pair<SortOrder, SortType>>? = null,
)
