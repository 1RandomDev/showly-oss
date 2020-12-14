package com.michaldrabik.ui_my_shows.archive

import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.michaldrabik.ui_base.BaseViewModel
import com.michaldrabik.ui_base.images.ShowImagesProvider
import com.michaldrabik.ui_base.utilities.ActionEvent
import com.michaldrabik.ui_base.utilities.extensions.findReplace
import com.michaldrabik.ui_model.Image
import com.michaldrabik.ui_model.ImageType.POSTER
import com.michaldrabik.ui_model.SortOrder
import com.michaldrabik.ui_my_shows.archive.cases.ArchiveLoadShowsCase
import com.michaldrabik.ui_my_shows.archive.cases.ArchiveRatingsCase
import com.michaldrabik.ui_my_shows.archive.cases.ArchiveSortOrderCase
import com.michaldrabik.ui_my_shows.archive.recycler.ArchiveListItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class ArchiveViewModel @Inject constructor(
  private val sortOrderCase: ArchiveSortOrderCase,
  private val loadShowsCase: ArchiveLoadShowsCase,
  private val ratingsCase: ArchiveRatingsCase,
  private val imagesProvider: ShowImagesProvider
) : BaseViewModel<ArchiveUiModel>() {

  fun loadShows(scrollToTop: Boolean = false) {
    viewModelScope.launch {
      val items = loadShowsCase.loadShows().map {
        val image = imagesProvider.findCachedImage(it.first, POSTER)
        ArchiveListItem(it.first, image, false, it.second)
      }

      uiState = ArchiveUiModel(items = items, scrollToTop = ActionEvent(scrollToTop))

      loadRatings(items)
    }
  }

  private fun loadRatings(items: List<ArchiveListItem>) {
    if (items.isEmpty()) return
    viewModelScope.launch {
      try {
        val listItems = ratingsCase.loadRatings(items)
        uiState = ArchiveUiModel(items = listItems)
      } catch (error: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(error)
      }
    }
  }

  fun loadSortOrder() {
    viewModelScope.launch {
      val sortOrder = sortOrderCase.loadSortOrder()
      uiState = ArchiveUiModel(sortOrder = ActionEvent(sortOrder))
    }
  }

  fun loadMissingImage(item: ArchiveListItem, force: Boolean) {

    fun updateItem(new: ArchiveListItem) {
      val currentItems = uiState?.items?.toMutableList()
      currentItems?.findReplace(new) { it.isSameAs(new) }
      uiState = uiState?.copy(items = currentItems)
    }

    viewModelScope.launch {
      updateItem(item.copy(isLoading = true))
      try {
        val image = imagesProvider.loadRemoteImage(item.show, item.image.type, force)
        updateItem(item.copy(isLoading = false, image = image))
      } catch (t: Throwable) {
        updateItem(item.copy(isLoading = false, image = Image.createUnavailable(item.image.type)))
      }
    }
  }

  fun setSortOrder(sortOrder: SortOrder) {
    viewModelScope.launch {
      sortOrderCase.setSortOrder(sortOrder)
      loadShows(scrollToTop = true)
    }
  }
}
